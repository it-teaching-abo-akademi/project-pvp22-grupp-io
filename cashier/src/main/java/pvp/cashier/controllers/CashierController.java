package pvp.cashier.controllers;

import com.almasb.fxgl.entity.action.Action;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pvp.cashier.models.CardReader;
import pvp.cashier.models.Order;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import pvp.models.PaymentType;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Payment;
import pvp.models.interfaces.Product;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class CashierController implements Initializable {

    private static final URL cashBoxStatusUrl;
    private static final URL cashBoxOpenUrl;
    private static final URL cradReaderStatusUrl;
    private static final URL cradReaderResultUrl;
    private static final URL cradReaderResetUrl;
    private static final URL cradReaderAbortUrl;
    private static final URL cradReaderStartUrl;


    static {
        try {
            cashBoxStatusUrl = new URL("http://localhost:9001/cashbox/status");
            cashBoxOpenUrl = new URL("http://localhost:9001/cashbox/open");
            cradReaderResultUrl = new URL("http://localhost:9002/cardreader/result");
            cradReaderResetUrl = new URL("http://localhost:9002/cardreader/reset");
            cradReaderStatusUrl = new URL("http://localhost:9002/cardreader/status");
            cradReaderAbortUrl = new URL("http://localhost:9002/cardreader/abort");
            cradReaderStartUrl = new URL("http://localhost:9002/cardreader/waitForPayment");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button SaveOrderButton;
    @FXML
    private TextField skuInput;
    @FXML
    private TextField cashAmount;
    @FXML
    private TextField amountToCustomer;
    @FXML
    private TextField cardStatus;
    @FXML
    private TableView<OrderLine> prodTableView;
    @FXML
    private TableColumn<OrderLine, String> NameColumn;
    @FXML
    private TableColumn<OrderLine, Integer> amountColumn;
    @FXML
    private TableColumn<OrderLine, Integer> priceColumn;
    @FXML
    private TableColumn<OrderLine, Integer> discountColumn;

    private CustomerController customerController;
    private Order order;
    private List<Product> searchedProducts = new ArrayList<Product>();
    private List<pvp.models.interfaces.Order> searchedOrders = new ArrayList<pvp.models.interfaces.Order>();


    public void setModel(Order model) {
        order = model;
        customerController.setModel(order);
        updateOrderLines();
    }

    public void setCustomerController(CustomerController customerController){
        this.customerController = customerController;
    }

    @FXML
    private void newOrder(){
        order = new Order();
        customerController.setModel(order);
        updateOrderLines();
    }

    @FXML
    public void saveOrder(ActionEvent event) throws IOException {
        JSONObject json = new JSONObject();
        JSONObject user = new JSONObject();
        List<JSONObject> payments = new ArrayList<JSONObject>();
        List<JSONObject> orderlines = new ArrayList<JSONObject>();

        json.accumulate("pk", this.order.getPk());
        json.accumulate("order_total", this.order.getTotalPrice());
        json.accumulate("userId", this.order.getUserId());
        json.accumulate("complete", this.order.isComplete());
        user.accumulate("pk", this.order.getUser().getPk());
        user.accumulate("customerReference", this.order.getUser().getCustomerReference());
        user.accumulate("name", this.order.getUser().getName());

        for (Payment payment: this.order.getPayments() ) {
            JSONObject jsonPayment = new JSONObject();
            jsonPayment.accumulate("pk", payment.getPk());
            jsonPayment.accumulate("paymentType", payment.getPaymentType());
            jsonPayment.accumulate("amount", payment.getAmount());
            payments.add(jsonPayment);
        }

        for (OrderLine orderLine: this.order.getOrderLines()) {
            JSONObject jsonOrderLine = new JSONObject();
            jsonOrderLine.accumulate("pk", orderLine.getPk());
            jsonOrderLine.accumulate("unitPrice", orderLine.getUnitPrice());
            jsonOrderLine.accumulate("quantity", orderLine.getQuantity());
            jsonOrderLine.accumulate("totalPrice", orderLine.getTotalPrice());
            jsonOrderLine.accumulate("productId", orderLine.getProductId());
            orderlines.add(jsonOrderLine);
        }
        json.accumulate("user", user);
        json.accumulate("order_lines", orderlines);
        json.accumulate("payment_lines", payments);

        URL postURL = new URL("http://localhost:8080/api/orders");
        HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection();
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(json.toString().getBytes());
        os.flush();
        os.close();
        System.out.println(httpURLConnection.getResponseCode());
    }

    @FXML
    private void doSearch(ActionEvent event) throws IOException {
        searchedProducts = new ArrayList<Product>();
        URL url = new URL("http://127.0.0.1:8080/api/products/search/" + this.skuInput.getText());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            JSONArray json = new JSONArray(response.toString());
            if (json.length() == 1) {
                JSONObject element = (JSONObject) json.get(0);
                String name = element.optString("name", "");
                String sku = element.optString("sku", "");

                addSelectedProduct(new pvp.models.Product(
                        element.getInt("pk"),
                        element.getInt("price"),
                        name,
                        sku
                ));
            }
            else {
                json.forEach(object -> {
                    JSONObject element = (JSONObject) object;
                    String name = element.optString("name", "");
                    String sku = element.optString("sku", "");

                    searchedProducts.add(new pvp.models.Product(
                            element.getInt("pk"),
                            element.getInt("price"),
                            name,
                            sku
                    ));
                });
                openProductList();
            }
        } else {
            System.out.println("GET request not worked");
            openProductList();
        }
    }

    protected void addSelectedProduct(Product product){
        order.addProduct(product);
        updateOrderLines();
    }

    private void updateOrderLines(){
        prodTableView.getItems().setAll(this.order.getOrderLines());
        customerController.updateOrderLines();
    }

    private void openProductList() throws IOException{
        FXMLLoader prodListLoader = new FXMLLoader(getClass().getResource("ProductPopup.fxml"));
        Parent prod = prodListLoader.load();

        ProductPopupController popupController = prodListLoader.getController();
        popupController.setSearchedProducts(searchedProducts);
        popupController.setMainController(this);

        Stage prodListStage = new Stage();
        prodListStage.setScene(new Scene(prod));
        prodListStage.show();
    }

    @FXML
    private void openOrderList(ActionEvent event) throws IOException{
        searchedOrders = new ArrayList<pvp.models.interfaces.Order>();
        URL url = new URL("http://127.0.0.1:8080/api/orders/incomplete/");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray json = new JSONArray(response.toString());
            json.forEach(object -> {
                JSONObject element = (JSONObject) object;
                String name = element.optString("name", "");
                String sku = element.optString("sku", "");


            });
        }

        FXMLLoader orderListLoader = new FXMLLoader(getClass().getResource("OrdersPopup.fxml"));
        Parent prod = orderListLoader.load();

        SavedOrdersPopupController popupController = orderListLoader.getController();

        searchedOrders.add(order);

        popupController.setSearchedOrders(searchedOrders);
        popupController.setMainController(this);

        Stage prodListStage = new Stage();
        prodListStage.setScene(new Scene(prod));
        prodListStage.show();

    }

    @FXML
    private void payWithCash(ActionEvent event) throws IOException {
        String amountString = cashAmount.getText();
        try {
            int amount = Integer.parseInt(amountString);
            boolean cashBoxIsOpen = false;
            HttpURLConnection httpURLConnection = (HttpURLConnection) cashBoxStatusUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in .readLine()) != null) {
                    response.append(inputLine);
                } in .close();
                cashBoxIsOpen = response.toString().equals("OPEN");
            }

            if (!cashBoxIsOpen) {
                httpURLConnection = (HttpURLConnection) cashBoxOpenUrl.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.getResponseCode();
            }
            int totalPrice = this.order.getTotalPrice();
            int paidAmount = this.order.getTotalPaidAmount();
            int totalLeftToPay = totalPrice - paidAmount;

            if (totalLeftToPay >= amount) {
                this.order.createPayment(amount, PaymentType.CASH);
            } else {
                this.order.createPayment(totalLeftToPay, PaymentType.CASH);
                amountToCustomer.setText(String.valueOf(amount - totalLeftToPay));
            }
            updateOrderLines();
        } catch (NumberFormatException e){}
    }

    @FXML
    private void saveReceipt(ActionEvent event) throws DocumentException, IOException {
        String date = new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss").format(new Date());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("../receipts/receipt-" + date + ".pdf");
        } catch (FileNotFoundException e) {
            fos = new FileOutputStream("~\\..\\receipts\\receipt_" + date + ".pdf");
        }

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        PdfWriter writer = PdfWriter.getInstance(doc, fos);

        date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

        doc.open();
        //adding paragraphs to the PDF
        doc.add(new Paragraph("Order from grupp 10's cashier"));
        doc.add(new Paragraph("                                       "));
        doc.add(new Paragraph("Purchase date: " + date));
        doc.add(new Paragraph("Items"));
        doc.add(new Paragraph(String.format("----------------------------------------------------------------------------")));
        for (OrderLine orderLine: this.order.getOrderLines()) {
            doc.add(new Paragraph(String.format(
                    "%s\n%s %50d * %d = %d",
                    orderLine.getProduct().getName(),
                    orderLine.getProduct().getSku(),
                    orderLine.getUnitPrice(),
                    orderLine.getQuantity(),
                    orderLine.getTotalPrice()
            )));
        }
        doc.add(new Paragraph("\n        Order total: " + order.getTotalPrice()));
        doc.add(new Paragraph("Payments:"));
        for (Payment payment: this.order.getPayments()) {
            doc.add(new Paragraph(String.format("    - %s: %s, %9s: %s",
                    "Payment type", payment.getPaymentType(),
                    "Amount", payment.getAmount())));
        }
        doc.add(new Paragraph("VAT: "));
        doc.add(new Paragraph());

        if (order.getUser() != null) {
            doc.add(new Paragraph("User no." + order.getUser().getCustomerReference()));
        }
        //closes the document
        doc.close();
        //closes the output stream
        fos.close();
    }

    @FXML
    private void payWithCard(ActionEvent event) throws IOException, JAXBException, InterruptedException, ParserConfigurationException, SAXException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) cradReaderStatusUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();
        if (response.toString().equals("WAITING_FOR_PAYMENT")) {
            httpURLConnection = (HttpURLConnection) cradReaderAbortUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.getResponseCode();
            payWithCash(event);
        } else if (response.toString().equals("DONE")) {
            httpURLConnection = (HttpURLConnection) cradReaderResetUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.getResponseCode();
            payWithCash(event);
        }

        httpURLConnection = (HttpURLConnection) cradReaderStartUrl.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        int amountToPay = this.order.getTotalPrice() + this.order.getTotalPaidAmount();
        os.write(("amount=" + amountToPay).getBytes());
        os.flush();
        os.close();
        httpURLConnection.getResponseCode();

        while (true) {
            httpURLConnection = (HttpURLConnection) cradReaderStatusUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.getResponseCode();
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            if (response.toString().equals("DONE")) {
                break;
            }
            cardStatus.setText(response.toString());
            System.out.println(response.toString());
            TimeUnit.SECONDS.sleep(1);
        }

        httpURLConnection = (HttpURLConnection) cradReaderResultUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");
        in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        response = new StringBuffer();

        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(response.toString())));
        doc.getDocumentElement().normalize();
        System.out.println(response.toString());
        Element result = (Element) doc.getElementsByTagName("result").item(0);
        // String bonusCardNumber = result.getElementsByTagName("bonusCardNumber").item(0).getTextContent();
        // String bonusState = result.getElementsByTagName("bonusState").item(0).getTextContent();
        String paymentCardNumber = result.getElementsByTagName("paymentCardNumber").item(0).getTextContent();
        // String goodThruMonth = result.getElementsByTagName("goodThruMonth").item(0).getTextContent();
        // String goodThruYear = result.getElementsByTagName("goodThruYear").item(0).getTextContent();
        String paymentState = result.getElementsByTagName("paymentState").item(0).getTextContent();
        String paymentCardType = result.getElementsByTagName("paymentCardType").item(0).getTextContent();

        cardStatus.setText(paymentState);
        if (paymentState.equals("ACCEPTED")) {
            this.order.createPayment(amountToPay, PaymentType.CARD);
        }
        updateOrderLines();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        amountColumn.setCellValueFactory(param -> {
            ObservableValue<Integer> q = new ReadOnlyObjectWrapper<Integer>(param.getValue().getQuantity());
            return q;
        });
        priceColumn.setCellValueFactory(param -> {
            ObservableValue<Integer> q = new ReadOnlyObjectWrapper<Integer>(param.getValue().getTotalPrice());
            return q;
        });
        discountColumn.setCellValueFactory(param -> {
            OrderLine orderline = param.getValue();
            Product product = orderline.getProduct();
            ObservableValue<Integer> q = new ReadOnlyObjectWrapper<Integer>(product.getPrice() * orderline.getQuantity() - orderline.getTotalPrice());
            return q;
        });
        NameColumn.setCellValueFactory(param -> {
            String name = param.getValue().getProduct().getName() + "(" + param.getValue().getProduct().getSku() + ")";
            ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(name);
            return q;
        });

        prodTableView.setRowFactory(tableView -> {
            final TableRow<OrderLine> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction(e -> {
                order.removeOrderLine(row.getItem());
                order.updateTotalPrice();
                updateOrderLines();
            });

            rowMenu.getItems().addAll(removeItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));
            return row;
        });

    }
}
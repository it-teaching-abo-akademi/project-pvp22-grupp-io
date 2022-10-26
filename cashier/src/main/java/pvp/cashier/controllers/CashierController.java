package pvp.cashier.controllers;

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
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pvp.cashier.models.Order;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


import java.awt.image.BufferedImage;

import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import pvp.models.PaymentType;
import pvp.models.User;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Payment;
import pvp.models.interfaces.Product;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class CashierController implements Initializable {

    private static final URL cashBoxStatusUrl;
    private static final URL cashBoxOpenUrl;
    private static final URL cardReaderStatusUrl;
    private static final URL cardReaderResultUrl;
    private static final URL cardReaderResetUrl;
    private static final URL cardReaderAbortUrl;
    private static final URL cardReaderStartUrl;


    static {
        try {
            cashBoxStatusUrl = new URL("http://localhost:9001/cashbox/status");
            cashBoxOpenUrl = new URL("http://localhost:9001/cashbox/open");
            cardReaderResultUrl = new URL("http://localhost:9002/cardreader/result");
            cardReaderResetUrl = new URL("http://localhost:9002/cardreader/reset");
            cardReaderStatusUrl = new URL("http://localhost:9002/cardreader/status");
            cardReaderAbortUrl = new URL("http://localhost:9002/cardreader/abort");
            cardReaderStartUrl = new URL("http://localhost:9002/cardreader/waitForPayment");
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
    private TableColumn<OrderLine, String> priceColumn;
    @FXML
    private TableColumn<OrderLine, String> discountColumn;
    @FXML
    private TextField discountAmount;
    @FXML
    private TextField amountLeftToPay;
    @FXML
    private TextField orderTotal;

    private CustomerController customerController;
    private pvp.models.interfaces.Order order;
    private List<Product> searchedProducts = new ArrayList<Product>();
    private List<pvp.models.interfaces.Order> searchedOrders = new ArrayList<pvp.models.interfaces.Order>();


    public void setModel(pvp.models.interfaces.Order model) {
        order = model;
        customerController.setModel(order);
        updateOrderLines();
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    @FXML
    private void newOrder() {
        order = new Order();
        customerController.setModel(order);
        updateOrderLines();
    }

    /**
     * saveOrder()
     * Method for saving incomplete orders, is triggered by the Save Order button in
     * the POSApplication and saves the current order
     * Saves order with all orderlines, all payments and the user to the postgreSQL.
     */
    @FXML
    public void saveOrder(ActionEvent event) throws IOException {
        JSONObject json = new JSONObject();
        List<JSONObject> payments = new ArrayList<JSONObject>();
        List<JSONObject> orderlines = new ArrayList<JSONObject>();

        json.accumulate("pk", this.order.getPk());
        json.accumulate("order_total", this.order.getTotalPrice());
        json.accumulate("userId", this.order.getUserId());
        json.accumulate("complete", this.order.isComplete());

        for (Payment payment : this.order.getPayments()) {
            JSONObject jsonPayment = new JSONObject();
            jsonPayment.accumulate("pk", payment.getPk());
            jsonPayment.accumulate("paymentType", payment.getPaymentType());
            jsonPayment.accumulate("amount", payment.getAmount());
            payments.add(jsonPayment);
        }

        for (OrderLine orderLine : this.order.getOrderLines()) {
            JSONObject jsonOrderLine = new JSONObject();
            jsonOrderLine.accumulate("pk", orderLine.getPk());
            jsonOrderLine.accumulate("unitPrice", orderLine.getUnitPrice());
            jsonOrderLine.accumulate("quantity", orderLine.getQuantity());
            jsonOrderLine.accumulate("totalPrice", orderLine.getTotalPrice());
            jsonOrderLine.accumulate("productId", orderLine.getProductId());
            orderlines.add(jsonOrderLine);
        }
        if (this.order.getUser() != null ) {
            json.accumulate("user", this.order.getUser().getJsonOfObject());
        }
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
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            this.order.setUser() response.toString()
        }

    }

    /**
     * doSearch()
     *
     * Selects all the products that match with either name or sku and adds them to
     * a pop-up window, where you can select the correct product.
     * Triggered by the search item button in the cashier view
     */
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

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray json = new JSONArray(response.toString());
            if (json.length() == 1) {
                JSONObject element = (JSONObject) json.get(0);
                String name = element.optString("name", "");
                String sku = element.optString("sku", "");

                addSelectedProduct(new pvp.models.Product(
                        element.getInt("pk"),
                        element.getInt("price"),
                        name,
                        sku,
                        element.getInt("soldCount")
                ));
            } else {
                json.forEach(object -> {
                    JSONObject element = (JSONObject) object;
                    String name = element.optString("name", "");
                    String sku = element.optString("sku", "");

                    searchedProducts.add(new pvp.models.Product(
                            element.getInt("pk"),
                            element.getInt("price"),
                            name,
                            sku,
                            element.getInt("soldCount")
                    ));
                });
                openProductList();
            }
        } else {
            System.out.println("GET request not worked");
            openProductList();
        }
        skuInput.clear();
    }

    /**
     * addSelectedProduct()
     * Adds selected product to the current order from the doSearch pop-up window
     */
    protected void addSelectedProduct(Product product) {
        order.addProduct(product);
        updateOrderLines();
    }

    /**
     * Updates the Orderlines and converts the price from cents to euros in the
     * GUI
     */
    private void updateOrderLines() {
        amountLeftToPay.setText(priceRounder(this.order.getTotalPrice()));
        orderTotal.setText(priceRounder(this.order.getTotalPrice() - this.order.getTotalPaidAmount()));
        prodTableView.getItems().setAll(this.order.getOrderLines());
        customerController.updateOrderLines();
    }

    /**
     * Called by the doSearch method and displays all the products in a pop-up window
     */
    private void openProductList() throws IOException {
        FXMLLoader prodListLoader = new FXMLLoader(getClass().getResource("ProductPopup.fxml"));
        Parent prod = prodListLoader.load();

        ProductPopupController popupController = prodListLoader.getController();
        popupController.setSearchedProducts(searchedProducts);
        popupController.setMainController(this);

        Stage prodListStage = new Stage();
        prodListStage.setScene(new Scene(prod));
        prodListStage.show();
    }

    /**
     * deserializeOrder()
     * Enables deserializer to Order by adding it as a JSONObject.
     */
    private pvp.models.interfaces.Order deserializeOrder(JSONObject jsonOrder) {
        pvp.models.interfaces.Order order = new Order(
                jsonOrder.getInt("pk"),
                jsonOrder.getInt("totalPrice"),
                new HashSet<OrderLine>(),
                User.getObjectFromJson(jsonOrder.getJSONObject("user")),
                new HashSet<Payment>(),
                jsonOrder.getBoolean("complete")
        );
        JSONArray orderLines = jsonOrder.getJSONArray("order_lines");
        for (int i = 0; i < orderLines.length(); i++) {
            JSONObject jsonOrderLine = orderLines.getJSONObject(i);
            JSONObject jsonProduct = jsonOrderLine.getJSONObject("product");
            String name = "";
            if (!jsonProduct.isNull("name")) {
                name = jsonProduct.getString("name");
            }

            Product product = new pvp.models.Product(
                    jsonProduct.getInt("pk"),
                    jsonProduct.getInt("price"),
                    name,
                    jsonProduct.getString("sku"),
                    jsonProduct.getInt("soldCount")
            );
            OrderLine orderLine = new pvp.models.OrderLine(
                    jsonOrderLine.getInt("pk"),
                    jsonOrderLine.getInt("unitPrice"),
                    jsonOrderLine.getInt("quantity"),
                    jsonOrderLine.getInt("totalPrice"),
                    product
            );
            order.addOrderLine(orderLine);
        }
        JSONArray jsonPaymentArray = jsonOrder.getJSONArray("payment_lines");
        for (int i = 0; i < jsonPaymentArray.length(); i++) {
            JSONObject jsonPayment = jsonPaymentArray.getJSONObject(i);
            order.createPayment(
                    jsonPayment.getInt("pk"),
                    jsonPayment.getInt("amount"),
                    jsonPayment.getString("payment_type_id")
            );
        }

        return order;
    }

    /**
     * openOrderList()
     * Method triggered by the View Orders button and displays the saved orders in a
     * pop-up window
     */
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
                searchedOrders.add(deserializeOrder(element));
            });
        }

        FXMLLoader orderListLoader = new FXMLLoader(getClass().getResource("OrdersPopup.fxml"));
        Parent prod = orderListLoader.load();

        SavedOrdersPopupController popupController = orderListLoader.getController();

        popupController.setSearchedOrders(searchedOrders);
        popupController.setMainController(this);

        Stage prodListStage = new Stage();
        prodListStage.setScene(new Scene(prod));
        prodListStage.show();

    }

    /**
     * PayWithCash()
     * Pay with payment type CASH.
     * Update Orderlist, so that changes are shown for the Cashier.
     */
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
            System.out.println(paidAmount);
            int totalLeftToPay = totalPrice - paidAmount;
            if (totalLeftToPay >= amount) {
                this.order.createPayment(amount, PaymentType.CASH);
            } else {
                this.order.createPayment(totalLeftToPay, PaymentType.CASH);
                amountToCustomer.setText(String.valueOf((amount - totalLeftToPay))+ "€");
                saveOrder(event);
            }
            updateOrderLines();
            cashAmount.clear();
        } catch (NumberFormatException e){}
    }

    /**
     * saveReceipt()
     * Saves receipt.
     * Streams in file directory, first trying Linux directory, then Microsoft directory.
     * Writes in the required data for the receipt into the file.
     * Closes the file and then renders an .png version of the receipt
     */
    @FXML
    private void saveReceipt(ActionEvent event) throws DocumentException, IOException {
        String fileDate = new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss").format(new Date());
        FileOutputStream fos;
        boolean linux;

        try {
            fos = new FileOutputStream("../receipts/receipt-" + fileDate + ".pdf"); //Linux
            linux = true;
        } catch (FileNotFoundException e) {
            fos = new FileOutputStream("~\\..\\receipts\\receipt_" + fileDate + ".pdf"); //Microsoft
            linux = false;
        }

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        PdfWriter writer = PdfWriter.getInstance(doc, fos);

        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

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
            doc.add(new Paragraph("User no." + order.getUser().getPk()));
        }

        // closes the document
        doc.close();
        // closes the output stream
        fos.close();

        String sourceDir;
        String destinationDir; // converted images from pdf document are saved here

        if (linux) {
            sourceDir = "../receipts/receipt-" + fileDate + ".pdf"; // Pdf files are read from this folder
            destinationDir = "../receipts/receipt-" + fileDate + ".png";
        } else {
            sourceDir = "~\\..\\receipts\\receipt_" + fileDate + ".pdf";
            destinationDir = "~\\..\\receipts\\receipt_" + fileDate + ".png";
        }

        try (final PDDocument document = PDDocument.load(new File(sourceDir))) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = renderer.renderImageWithDPI(page, 300, ImageType.RGB);
                ImageIOUtil.writeImage(bim, destinationDir, 300);
            }

        } catch (IOException e){
                System.err.println("Exception while trying to create pdf document - " + e);
        }

    }

    /**
     * payWithCard()
     * Pay with payment type CARD.
     * WAITING_FOR_PAYMENT: the process stops until the customer has paid (the "swipe card"-button has been pressed).
     * DONE: the payment has been completed and registered.
     * IDEAL: the status of the card is IDEAL when it is ready for payment-requests.
     */
    @FXML
    private void payWithCard(ActionEvent event) throws IOException, JAXBException, InterruptedException, ParserConfigurationException, SAXException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) cardReaderStatusUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();
        if (response.toString().equals("WAITING_FOR_PAYMENT")) {
            httpURLConnection = (HttpURLConnection) cardReaderAbortUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.getResponseCode();
            payWithCard(event);
            return;
        } else if (response.toString().equals("DONE")) {
            httpURLConnection = (HttpURLConnection) cardReaderResetUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.getResponseCode();
            payWithCard(event);
            return;
        }

        httpURLConnection = (HttpURLConnection) cardReaderStartUrl.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        int amountToPay = this.order.getTotalPrice() - this.order.getTotalPaidAmount();
        os.write(("amount=" + amountToPay).getBytes());
        os.flush();
        os.close();
        httpURLConnection.getResponseCode();

        while (true) {
            httpURLConnection = (HttpURLConnection) cardReaderStatusUrl.openConnection();
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
            TimeUnit.SECONDS.sleep(1);
        }

        httpURLConnection = (HttpURLConnection) cardReaderResultUrl.openConnection();
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
            this.order.createPayment(amountToPay, PaymentType.DEBIT);
        }
        saveOrder(event);
        updateOrderLines();
    }

    /**
     * initialize()
     * Connects the cells of the table in the front-end to their variables on the back-end.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        amountColumn.setCellValueFactory(param -> {
            ObservableValue<Integer> q = new ReadOnlyObjectWrapper<Integer>(param.getValue().getQuantity());
            return q;
        });
        priceColumn.setCellValueFactory(param -> {
            ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(priceRounder(param.getValue().getTotalPrice()));
            return q;
        });
        discountColumn.setCellValueFactory(param -> {
            OrderLine orderline = param.getValue();
            Product product = orderline.getProduct();

            ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(priceRounder(product.getPrice() * orderline.getQuantity() - orderline.getTotalPrice()));
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

    /**
     * addDiscount()
     * Fetches discount as discountString.
     * Fetches list of items selected to be discounted.
     */
    @FXML
    public void addDiscount(ActionEvent actionEvent) {
        String discountString = discountAmount.getText();
        int amount = (int) (Float.parseFloat(discountString) * 100);

        OrderLine itemToDiscount = prodTableView.getSelectionModel().getSelectedItem();
        if (itemToDiscount == null) {
            System.out.println("No item selected");
            return;
        }
        int currentPrice = itemToDiscount.getUnitPrice();
        if (amount < 0 ) {
            amount = Math.abs(amount);
        }
        if (currentPrice < amount) {
            amount = currentPrice;
        }

        int newPrice = currentPrice-amount;
        itemToDiscount.setUnitPrice(newPrice);
        itemToDiscount.calculatePrice();
        order.updateTotalPrice();

        updateOrderLines();
        discountAmount.clear();
    }

    public String priceRounder(int price) {
        return String.format("$%.2f€ ", price * 0.01);
    }
}
package pvp.models;


import org.junit.jupiter.api.Test;
import pvp.models.interfaces.BonusCard;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;

public class OrderTest {
    OrderLine orderLine = new OrderLine(1, 2, 3,
            4, 5);
    ZonedDateTime date;
    User user = new User(1, "Bubben", "Belzebub", Sex.UNSPECIFIED, date,
            new HashSet<BonusCard>(), 5000);
    PaymentType paymentType = PaymentType.CASH;
    HashSet<Payment> paymentSet = new HashSet<>();
    HashSet<BonusCard> bonuscards = new HashSet<BonusCard>();
    Product product = new Product(20, 20, "Hula hula ring", "22", 23);
    Order order = new Order(6, 7, new HashSet<pvp.models.interfaces.OrderLine>(), user, new HashSet<pvp.models.interfaces.Payment>(), true);
    Payment payment = new Payment(11, 12, paymentType, order);
    BonusCard bonuscard = new pvp.models.BonusCard(1, "2", "Bubben", 3, 2030, false, false, user);

    @Test
    public void testOrder() {
        assert order.getOrderLines().size() == 0; // size is 0
        assert !order.getOrderLines().contains(orderLine); // order does not contain orderline
        order.addOrderLine(orderLine); // adds orderLine to order
        assert order.getOrderLines().size() == 1;  // total of 1 orderLine in the order
        assert order.getOrderLines().contains(orderLine);  // order now contains orderline

        order.addProduct(product); // adds product worth 20
        order.updateTotalPrice(); // updates the order -> 20 + 4 = 24
        assert order.getTotalPrice() == 24;  // the total price is 24

        assert order.getOrderLineById(1).isPresent(); // the orderline with pk 1 is in order

        order.setUser(user);  // sets the user to order
        assert order.getUser() == user; // the user of order is user
        assert order.getUserId() == 1; // the user of order has the ID 1
        order.setUser(null);    // sets user of order to null
        assert order.getUser() == null;  // the user of order is now null

        order.createPayment(5, PaymentType.CASH); // adds payment
        assert order.getPayments().size() == 1;   // fetches the amount of payments in order

        order.setIsComplete(true); // sets order to complete
        assert order.isComplete(); // order is now complete

        order.removeOrderLine(orderLine);  // removes orderline by orderline from order
        assert !order.getOrderLines().contains(orderLine); // order does not contain orderline anymore
        order.addOrderLine(orderLine);  // add orderlina to order
        order.removeOrderLine(1); // removes orderline by id from order
        assert order.getOrderLines().contains(orderLine);  // order does not contain orderline anymore.
    }
    @Test
    public void testOrderLine() {
        assert orderLine.getTotalPrice() == 4;
        orderLine.setProduct(product);  // product is set to orderline
        assert orderLine.getTotalPrice() == 60;  // total price is 60
        orderLine.setQuantity(4);      // quantity is set to 4
        assert orderLine.getTotalPrice() == 80;  // total price is 80
        orderLine.setProductId(2);  // product id is 2
        orderLine.setUnitPrice(10);  // unit price is 10
        assert orderLine.getTotalPrice() == 40; // 10 * 4
        assert orderLine.getProduct().getName() == "Hula hula ring"; // fetches the product on orderline
        assert orderLine.getUnitPrice() == 10; // unit price is 10
        assert orderLine.getProductId() == 2;   // product id is 2

        assert orderLine.getPk() == 1; // orderline pk is 1
    }
    @Test
    public void testPayment() {
        payment.setOrder(order);    // set order to payment
        assert payment.getOrder() == order; // payment is connected to order
        assert payment.getOrderId() == 6;   // the order ID is 6
        assert payment.getPaymentType() == PaymentType.CASH;  // the payment type of payment is CASH
        assert payment.getAmount() == 12;   // the amount in payment is 12

        payment.setAmount(5);  // amount = 5
        payment.setOrderId(4);  // order ID = 4
        assert payment.getAmount() == 5;  // amount in payment is 5
        assert payment.getOrderId() == 6;  // prioritizes the ID of the order, which is 6
        payment.setOrder(null);
        payment.setOrderId(4);
        assert payment.getOrderId() == 4;
    }
    @Test
    public void testProduct() {
        assert product.getName() == "Hula hula ring";   // name is string name
        assert product.getPrice() == 20;  //price is 20
        assert product.getSku() == "22";  // sku is 22
        assert product.getPk() == 20;   // pk is 20
        assert product.getSoldCount() == 23;  //total sold products 23

        product.setName("Get");  // name is set to "Get"
        product.setSku("50");   // set sku to 50
        product.setPrice(60);   // price is set to 60
        product.setSoldCount(70);   // total sold set to 70

        assert product.getName() == "Get";  // name is "Get"
        assert product.getSku() == "50";   // sku is 50
        assert product.getPrice() == 60;   // price is 60
        assert product.getSoldCount() == 70;   // total sold is 70
    }
    @Test
    public void testUser() {
        user.setBonusCards(bonuscards);  // sets bonuscards to user
        assert user.getBonusCards() == bonuscards; // bonuscards are the user's
        assert user.getPk() == 1;  // the user's pk is 1
        assert user.getBirthDay() == date;  // birthday is date
        assert user.getBonusPoints() == 5000;  // total bonus points are 5000
        assert user.getSex() == Sex.UNSPECIFIED;  // sex is unspecified
        assert user.getFirstName() == "Bubben";  // first name
        assert user.getLastName() == "Belzebub";  // last name

        user.setSex(Sex.FEMALE);  // change sex to female
        user.setBonusPoints(60000);  // change bonus points to 600000
        user.setFirstName("B");  // change first name to B
        user.setLastName("O");   // change last name to O

        assert user.getSex() == Sex.FEMALE;   // the sex of the user is now female
        assert user.getFirstName() == "B";  // the first name of the user is now B
        assert user.getLastName() == "O";   // the last name of the user is now O
        assert user.getBonusPoints() == 60000;   // the bonus points for the customer is now 600000
    }
    @Test
    public void testBonusCard() {
        assert bonuscard.getUser() == user;  // user is user
        assert bonuscard.getNumber() == "2";  // number of bonuscard is 2
        assert bonuscard.getPk() == 1;   // pk of bonuscard is 1
        assert bonuscard.getGoodThruMonth() == 3;   // good thru month is 3
        assert bonuscard.getGoodThruYear() == 2030;  //good thru year is 2030
        assert bonuscard.getHolderName()  == "Bubben";   // name of holder is Bubben
        assert bonuscard.getUserId() == 1;    // bnuscard's user's ID is 1

        bonuscard.setGoodThruMonth(5);  // set good thru month to 5
        bonuscard.setGoodThruYear(2040);   // set good thru year to 2040
        bonuscard.setHolderName("I");   // set holder name to I
        bonuscard.setNumber("10");   // set numbet of bonuscard to 10
        bonuscard.setIsBlocked(true);   // block the card
        bonuscard.setIsExpired(true);   // the card is expired

        assert bonuscard.getNumber() == "10";   // the number of the bonuscard is now 10
        assert bonuscard.getGoodThruMonth() == 5;   // the goodthrumonth is now 5
        assert bonuscard.getGoodThruYear() == 2040;   // the goodthruyear is now 2040
        assert bonuscard.getHolderName()  == "I";  // the holder name of the bonuscard is now I
        assert bonuscard.isBlocked();   // the bonuscard is now blocked
        assert bonuscard.isExpired();   // the bonuscard is now expired

    }
}
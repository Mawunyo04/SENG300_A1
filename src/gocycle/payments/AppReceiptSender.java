package gocycle.payments;

import gocycle.trips.Trip;

//delivers the receipt inside the riders app.
public class AppReceiptSender implements ReceiptSender {

    @Override
    public void send(Trip trip){
        String text=ReceiptText.build(trip);
        System.out.println("[APP RECEIPT] "+text);
    }
}
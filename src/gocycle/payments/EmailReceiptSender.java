package gocycle.payments;

import gocycle.trips.Trip;

//delivers the receipt to the riders email
public class EmailReceiptSender implements ReceiptSender {

    @Override
    public void send(Trip trip){
        //build the text first so a trip with no fare is rejected
        String text=ReceiptText.build(trip);
        String email= trip.getRider().getEmail();
        if (email==null || email.isBlank()){
            throw new IllegalStateException("Cannot email the receipt, the rider has no email address");
        }
        System.out.println("[EMAIL RECEIPT] To: "+ email+" | "+text);
    }
}
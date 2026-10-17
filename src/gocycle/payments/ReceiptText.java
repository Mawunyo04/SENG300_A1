package gocycle.payments;

import gocycle.trips.Trip;

/**
 * Helper class that builds the receipt wording once, so the app and email senders show the
 * same information and a wording change is made in one place.
 */
final class ReceiptText {

    private ReceiptText() {
    }
    //builds receipt text for each trip
    static String build(Trip trip){
        if (trip ==null) {
            throw new IllegalArgumentException("trip must not be null");
        }
        //once the trip has ended and been priced
        if (trip.getFare()==null){
            throw new IllegalStateException("Cannot send a receipt: trip "+trip.getId() +" has no fare yet");
        }
        return "GoCycle receipt - Trip " +trip.getId() + ", duration "+trip.getDurationMinutes() + " min" + ", total $"+trip.getFare().toPlainString() ;
    }
}
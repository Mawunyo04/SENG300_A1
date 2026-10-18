package gocycle.stations;

import gocycle.payments.PaymentMethodType;
import java.util.Map;

// small screen at a station. It only shows messages 
// chosen languages it supports: English ("en") & French ("fr") 
public class StationScreen {

    private Zone zone;      
    private String language; // "en", "fr" 

    // translation tables. the key is the English message.
    private Map<String, String> french;
    

    public StationScreen(Zone zone) {
       
    }

    // Fill the two translation tables
    private void loadTranslations() {
        
    }

    public String getLanguage() {
        return null; 
    }

    
    public void setLanguage(String code) {
      
    }

   //show translated msg with zones logo and color 
    public void show(String message) {
        
    }

    //"speaks" a translated msg out loud 
    public void readAloud(String message) {
        
    }

    // Tell a rider which station to go to because this one is full.
    // If target is null, no other station has space.
    public void showRedirect(Station target) {
      
    }

    //show payment methods accepted
    public void showPaymentOptions() {
       
    }

    
    private void display(String text) {
        
    }

    
    private void speak(String text) {
        
    }

    
    private String paymentLabel(PaymentMethodType method) {
       
        return null;
    }

    
    private String translate(String message) {
        
        return message;
    }
}
package gocycle.stations;

import gocycle.payments.PaymentMethodType;

import java.util.HashMap;
import java.util.Map;

// small screen at a station. It only shows messages, has no input
// chosen languages it supports: English ("en") & French ("fr") 
public class StationScreen {

    private Zone zone;      
    private String language; // "en", "fr" 

    // translation tables. the key is the English message.
    private Map<String, String> french;
    
    //constructor saves the zone, starts language in english
    public StationScreen(Zone zone) {
        this.zone = zone;
        this.language = "en";
        loadTranslations();
       
    }

    // Fill the french translation table
    //hashmap works like a dictionary
    private void loadTranslations() {
        french = new HashMap<String, String>();
        french.put("Welcome to GoCycle", "Bienvenue chez GoCycle");
        french.put("Station full", "Station pleine");
        french.put("Station full. Please go to station", "Station pleine. Veuillez aller a la station");
        french.put("No free station is available nearby", "Aucune station libre a proximite");
        french.put("Accepted payment methods", "Modes de paiement acceptes");
        french.put("None", "Aucun");
        french.put("Credit card", "Carte de credit");
        french.put("Transit card", "Carte de transport");
        french.put("Phone wallet", "Portefeuille mobile");
        
        
    }

    public String getLanguage() {
        return language; 
    }

    //set language english/french
    public void setLanguage(String code) {
        if (code.equals("en") || code.equals("fr")){
            language = code;
            System.out.println("[Screen] Language changed to " + code);
        }else{
            System.out.println("[Screen] Language '" + code + "' is not supported. Keeping '"
                    + language + "'.");
        }
      
    }

   //show translated msg with zones logo and color 
    public void show(String message) {
        display(translate(message));                  
    }

    //"speaks" a translated msg out loud 
    public void readAloud(String message) {
        speak(translate(message));

        
    }

    // Tell a rider which station to go to because this one is full.
    // If target is null, no other station has space.
    public void showRedirect(Station target) {
        String text;
        if (target == null) {
            text = translate("No free station is available nearby");
        } else {
            text = translate("Station full. Please go to station") + " " + target.getId();
        }
        display(text);
        speak(text);
    }

    //show payment methods accepted by this zone
    public void showPaymentOptions() {
        String text = translate("Accepted payment methods");
        boolean foundOne = false;
        //loops through payment options, adds a line for allowed options
        for (PaymentMethodType method : PaymentMethodType.values()) {
            if (zone.allows(method)) {
                text = text + "\n - " + translate(paymentLabel(method));
                foundOne = true;
            }
        }
        if (!foundOne) {
            text = text + "\n - " + translate("None");
        }
        display(text);
       
    }

    private void display(String text) {
        System.out.println("----------------------------------");
        System.out.println("Logo: " + zone.getLogoPath() + "   Colour: " + zone.getPrimaryColour());
        System.out.println(text);
        System.out.println("----------------------------------");
    }

    //simulate text to speech
    private void speak(String text) {
        System.out.println("[Screen says out loud] " + text);
    }

    
    private String paymentLabel(PaymentMethodType method) {
        if (method == PaymentMethodType.CREDIT_CARD) {
            return "Credit card";
        }
        if (method == PaymentMethodType.TRANSIT_CARD) {
            return "Transit card";
        }
        if (method == PaymentMethodType.PHONE_WALLET) {
            return "Phone wallet";
        }
        return method.toString();
    }

    
    private String translate(String message) {
        if (language.equals("fr") && french.containsKey(message)) {
            return french.get(message);
        }
        return message;//if msg is not in table, returns msg unchanged
    }
}
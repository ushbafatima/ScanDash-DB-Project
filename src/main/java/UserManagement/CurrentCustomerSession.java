package UserManagement;

import CardManagement.Card;

public class CurrentCustomerSession {
    private static CurrentCustomerSession instance;
    private Card currentCard;

    private CurrentCustomerSession() {}

    public static CurrentCustomerSession getInstance() {
        if (instance == null) {
            instance = new CurrentCustomerSession();
        }
        return instance;
    }

    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public String getCurrentCardId() {
        return currentCard != null ? currentCard.getCardID() : null;
    }

    public void clearSession() {
        currentCard = null;
    }
}

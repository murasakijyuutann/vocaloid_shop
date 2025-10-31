package mjyuu.vocaloidshop.entity;

public enum OrderStatus {
    PAYMENT_RECEIVED,   // payment confirmed
    PROCESSING,         // verifying, allocating stock
    PREPARING,          // picking & packing
    READY_FOR_DELIVERY, // handed to courier or staged
    IN_DELIVERY,        // in transit / out for delivery
    DELIVERED,          // delivered to recipient
    CANCELED            // canceled at any stage
}

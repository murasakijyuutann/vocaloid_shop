package mjyuu.vocaloidshop.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import mjyuu.vocaloidshop.entity.OrderStatus;

@Converter(autoApply = false)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderStatus attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String norm = dbData.trim().toUpperCase().replace('-', '_').replace(' ', '_');
        // Map common synonyms from previous schemas
        switch (norm) {
            case "PAID":
            case "PAYED":
            case "PAYMENT_CONFIRMED":
            case "PAYMENT":
            case "PAYMENT_RECEIVED":
                return OrderStatus.PAYMENT_RECEIVED;
            case "PROCESSING":
                return OrderStatus.PROCESSING;
            case "PREPARING":
            case "PACKING":
            case "PICKING":
                return OrderStatus.PREPARING;
            case "READY_FOR_DELIVERY":
            case "READY_FOR_SHIPMENT":
            case "READY_TO_SHIP":
                return OrderStatus.READY_FOR_DELIVERY;
            case "IN_DELIVERY":
            case "SHIPPED":
            case "OUT_FOR_DELIVERY":
            case "IN_TRANSIT":
                return OrderStatus.IN_DELIVERY;
            case "DELIVERED":
            case "COMPLETED":
                return OrderStatus.DELIVERED;
            case "CANCELED":
            case "CANCELLED":
                return OrderStatus.CANCELED;
            default:
                try { return OrderStatus.valueOf(norm); } catch (Exception ignored) {}
                // Fallback to PROCESSING to avoid crash; adjust as desired
                return OrderStatus.PROCESSING;
        }
    }
}

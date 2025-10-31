package mjyuu.vocaloidshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mjyuu.vocaloidshop.dto.AddressRequestDTO;
import mjyuu.vocaloidshop.dto.AddressResponseDTO;
import mjyuu.vocaloidshop.entity.Address;
import mjyuu.vocaloidshop.entity.User;
import mjyuu.vocaloidshop.repository.AddressRepository;
import mjyuu.vocaloidshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressResponseDTO> list(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return addressRepository.findByUserOrderByIdDesc(user).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponseDTO create(Long userId, AddressRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow();
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            // clear previous defaults
            addressRepository.findByUserOrderByIdDesc(user).forEach(a -> a.setDefault(false));
        }
        Address a = Address.builder()
                .user(user)
                .recipientName(dto.getRecipientName())
                .line1(dto.getLine1())
                .line2(dto.getLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .phone(dto.getPhone())
                .isDefault(Boolean.TRUE.equals(dto.getIsDefault()))
                .build();
        return toDto(addressRepository.save(a));
    }

    @Transactional
    public AddressResponseDTO update(Long userId, Long addressId, AddressRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow();
        Address a = addressRepository.findById(addressId).orElseThrow();
        if (!a.getUser().getId().equals(user.getId())) throw new RuntimeException("권한 없음");
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            addressRepository.findByUserOrderByIdDesc(user).forEach(x -> x.setDefault(false));
            a.setDefault(true);
        }
        if (dto.getRecipientName() != null) a.setRecipientName(dto.getRecipientName());
        if (dto.getLine1() != null) a.setLine1(dto.getLine1());
        if (dto.getLine2() != null) a.setLine2(dto.getLine2());
        if (dto.getCity() != null) a.setCity(dto.getCity());
        if (dto.getState() != null) a.setState(dto.getState());
        if (dto.getPostalCode() != null) a.setPostalCode(dto.getPostalCode());
        if (dto.getCountry() != null) a.setCountry(dto.getCountry());
        if (dto.getPhone() != null) a.setPhone(dto.getPhone());
        return toDto(addressRepository.save(a));
    }

    @Transactional
    public void delete(Long userId, Long addressId) {
        User user = userRepository.findById(userId).orElseThrow();
        Address a = addressRepository.findById(addressId).orElseThrow();
        if (!a.getUser().getId().equals(user.getId())) throw new RuntimeException("권한 없음");
        addressRepository.delete(a);
    }

    private AddressResponseDTO toDto(Address a) {
        return AddressResponseDTO.builder()
                .id(a.getId())
                .recipientName(a.getRecipientName())
                .line1(a.getLine1())
                .line2(a.getLine2())
                .city(a.getCity())
                .state(a.getState())
                .postalCode(a.getPostalCode())
                .country(a.getCountry())
                .phone(a.getPhone())
                .isDefault(a.isDefault())
                .build();
    }
}

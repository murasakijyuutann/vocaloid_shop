package mjyuu.vocaloidshop.repository;

import mjyuu.vocaloidshop.entity.Address;
import mjyuu.vocaloidshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserOrderByIdDesc(User user);
}
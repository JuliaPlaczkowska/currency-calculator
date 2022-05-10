package pl.example.currencycalculator.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.example.currencycalculator.model.entity.ActivityLog;

public interface ActivityLogRepo extends JpaRepository<ActivityLog, Long> {
}

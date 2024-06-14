import org.junit.jupiter.api.Test;
import tasks.*;
import managers.*;
import static org.junit.jupiter.api.Assertions.*;


class ManagersTest {
    @Test
    void managerReturnNotNullTask() { // менеджер возвращает ненулевой экземпляр класса InMemoryTaskManager
        assertNotNull(Managers.getDefault());
    }
    @Test
    void managerReturnNotNullHistory() { // менеджер возвращает ненулевой экземпляр класса InMemoryHistory
        assertNotNull(Managers.getDefaultHistory());
    } // тест


}

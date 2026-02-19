import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraTest {

    @Test
    void sumaCorrecta() {
        int resultado = 2 + 2;
        assertEquals(4, resultado);
    }
}

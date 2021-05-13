package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void poner_conMonto1500_saldoActulizadoCon1500() {
    cuenta.poner(1500);
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  void poner_conMontoNegativo_lanzaExcepcion() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void poner_tresMontos_saldoActualizadoConSumaDeMontos() {
    cuenta.poner(1500);
    cuenta.poner(400);
    cuenta.poner(1900);
    assertEquals(3800, cuenta.getSaldo());
  }

  @Test
  void poner_MasDeTresMontos_lanzaExcepcion() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void sacar_montoMayorQueSaldo_lanzaExcepcion() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void sacar_montoMenosDe1000() {
    assertDoesNotThrow(() -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(999);
    });
  }

  @Test
  public void sacar_monto200_actualizaSaldo() {
    cuenta.setSaldo(1000);
    cuenta.sacar(200);
    assertEquals(800, cuenta.getSaldo());
  }

  @Test
  void sacar_montoMenorSaldo_lanzaExcepcion() {
    cuenta.setSaldo(100);
    assertThrows(SaldoMenorException.class, () -> {
      cuenta.sacar(200);
    });
  }

  @Test
  public void sacar_montoMasDe1000_lanzaExcepcion() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void sacar_montoNegativo_lanzaExcepcion() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  public void getMontoExtraidoA_enFechaHoy_sumaMontosIngresadosHoy() {
    cuenta.setSaldo(1000);
    cuenta.sacar(150);
    cuenta.sacar(100);
    assertEquals(250, cuenta.getMontoExtraidoA(LocalDate.now()));
  }

}
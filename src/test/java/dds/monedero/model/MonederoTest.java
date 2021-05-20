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
  private Cuenta cuentaCon_5000;
  private Cuenta cuentaCon_1000;
  private Cuenta cuentaCon_100;
  private Cuenta cuentaCon_90;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
    cuentaCon_5000 = new Cuenta(5000);
    cuentaCon_1000 = new Cuenta(1000);
    cuentaCon_100 = new Cuenta(100);
    cuentaCon_90 = new Cuenta(90);
  };

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
          cuentaCon_90.sacar(1001);
    });
  }

  @Test
  public void sacar_montoMenosDe1000() {
    assertDoesNotThrow(() -> {
      cuentaCon_5000.sacar(999);
    });
  }

  @Test
  public void sacar_monto200_actualizaSaldo() {
    cuentaCon_1000.sacar(200);
    assertEquals(800, cuentaCon_1000.getSaldo());
  }

  @Test
  void sacar_montoMenorSaldo_lanzaExcepcion() {
    assertThrows(SaldoMenorException.class, () -> {
      cuentaCon_100.sacar(200);
    });
  }

  @Test
  public void sacar_montoMasDe1000_lanzaExcepcion() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuentaCon_5000.sacar(1001);
    });
  }

  @Test
  public void sacar_montoNegativo_lanzaExcepcion() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  public void getMontoExtraidoA_enFechaHoy_sumaMontosIngresadosHoy() {
    cuentaCon_1000.sacar(150);
    cuentaCon_1000.sacar(100);
    assertEquals(250, cuentaCon_1000.getMontoExtraidoA(LocalDate.now()));
  }

}
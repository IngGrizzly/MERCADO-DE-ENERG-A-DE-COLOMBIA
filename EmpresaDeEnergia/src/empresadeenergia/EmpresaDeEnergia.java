/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package empresadeenergia;
import java.awt.*;
import java.io.*;
import javax.swing.*;

public class EmpresaDeEnergia  {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmpresaDeEnergia::mostrarVentanaLogin);
    }

    private static void mostrarVentanaLogin() {
        JFrame frame = new JFrame("Login de Usuario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(4, 1, 10, 10));

        JTextField usuarioField = new JTextField();
        JPasswordField contrasenaField = new JPasswordField();

        frame.add(new JLabel("Usuario:"));
        frame.add(usuarioField);
        frame.add(new JLabel("Contraseña:"));
        frame.add(contrasenaField);

        JPanel botonesPanel = new JPanel();
        JButton loginBtn = new JButton("Entrar");
        JButton registerBtn = new JButton("Registrarse");
        botonesPanel.add(loginBtn);
        botonesPanel.add(registerBtn);
        frame.add(botonesPanel);

        loginBtn.addActionListener(e -> {
            String usuario = usuarioField.getText();
            String contrasena = new String(contrasenaField.getPassword());
            if (validarCredenciales(usuario, contrasena)) {
                JOptionPane.showMessageDialog(frame, "Ingreso exitoso");
                frame.dispose();
                mostrarPantallaMeses();
            } else {
                JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrectos");
            }
        });

        registerBtn.addActionListener(e -> {
            String usuario = usuarioField.getText();
            String contrasena = new String(contrasenaField.getPassword());
            if (!usuario.isEmpty() && !contrasena.isEmpty()) {
                registrarUsuario(usuario, contrasena);
                JOptionPane.showMessageDialog(frame, "Usuario registrado");
            } else {
                JOptionPane.showMessageDialog(frame, "Completa ambos campos");
            }
        });

        frame.setVisible(true);
    }

    private static boolean validarCredenciales(String usuario, String contrasena) {
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2 && partes[0].equals(usuario) && partes[1].equals(contrasena)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Archivo no encontrado.");
        }
        return false;
    }

    private static void registrarUsuario(String usuario, String contrasena) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
            writer.write(usuario + "," + contrasena);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarPantallaMeses() {
        JFrame frame = new JFrame("Matriz Meses");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Hola Tabla", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel panel = new JPanel(new GridLayout(3, 4));
        String[] meses = {
                "Enero", "Febrero", "Marzo", "Abril",
                "Mayo", "Junio", "Julio", "Agosto",
                "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        for (String mes : meses) {
            JButton button = new JButton(mes);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(e -> mostrarDias(mes));
            panel.add(button);
        }

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void mostrarDias(String mes) {
        int numDias = obtenerNumeroDias(mes);
        JFrame frame = new JFrame("Días de " + mes);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel diasPanel = new JPanel(new GridLayout(0, 7));

        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (String diaSemana : diasSemana) {
            JLabel label = new JLabel(diaSemana, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            diasPanel.add(label);
        }

        for (int i = 1; i <= numDias; i++) {
            String textoDia = String.valueOf(i);
            JButton diaButton = new JButton(textoDia);
            diaButton.setFont(new Font("Arial", Font.PLAIN, 16));
            int finalI = i;
            diaButton.addActionListener(e -> mostrarHoras(mes, String.valueOf(finalI)));
            diasPanel.add(diaButton);
        }

        JButton consultasButton = new JButton("Consultas");
        consultasButton.setFont(new Font("Arial", Font.BOLD, 16));
        consultasButton.addActionListener(e -> mostrarConsultas(mes));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(consultasButton);

        panel.add(diasPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void mostrarHoras(String mes, String dia) {
        JFrame frame = new JFrame("Horas del día " + dia + " de " + mes);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 6));
        for (int i = 0; i < 24; i++) {
            String horaTexto = String.format("%02d:00", i);
            JButton horaButton = new JButton(horaTexto);
            horaButton.setFont(new Font("Arial", Font.PLAIN, 14));
            int hora = i;
            horaButton.addActionListener(e -> mostrarInfo(mes, dia, hora));
            panel.add(horaButton);
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void mostrarInfo(String mes, String dia, int hora) {
        String franja;
        int precioPorKW;
        int consumoMin, consumoMax;

        if (hora >= 0 && hora <= 6) {
            franja = "Franja 1 (00:00 - 06:00)";
            consumoMin = 100;
            consumoMax = 300;
            precioPorKW = 200;
        } else if (hora >= 7 && hora <= 17) {
            franja = "Franja 2 (07:00 - 17:00)";
            consumoMin = 300;
            consumoMax = 600;
            precioPorKW = 300;
        } else {
            franja = "Franja 3 (18:00 - 23:00)";
            consumoMin = 600;
            consumoMax = 1000;
            precioPorKW = 500;
        }

        String mensaje = String.format(
                "Hora: %02d:00\nMes: %s\nDía: %s\n%s\nRango Consumo: %d - %d kW/H\nPrecio: %d COP por kW/H",
                hora, mes, dia, franja, consumoMin, consumoMax, precioPorKW
        );

        JOptionPane.showMessageDialog(null, mensaje, "Información de Consumo", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarConsultas(String mes) {
        JFrame consultasFrame = new JFrame("Consultas - " + mes);
        consultasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        consultasFrame.setSize(400, 300);
        consultasFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] opciones = {
                " Ver factura del mes",
                " Ver todos los clientes",
                " Estadísticas de consumo",
                " Volver"
        };

        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.setFont(new Font("Arial", Font.PLAIN, 16));
            boton.addActionListener(e -> {
                if (opcion.contains("Factura")) {
                    JOptionPane.showMessageDialog(null, "Mostrando factura del mes: " + mes);
                } else if (opcion.contains("clientes")) {
                    JOptionPane.showMessageDialog(null, "Listado de clientes del mes: " + mes);
                } else if (opcion.contains("Estadísticas")) {
                    mostrarEstadisticasConsumo(mes);
                } else if (opcion.contains("Volver")) {
                    consultasFrame.dispose();
                }
            });
            panel.add(boton);
        }

        consultasFrame.add(panel);
        consultasFrame.setVisible(true);
    }

    private static void mostrarEstadisticasConsumo(String mes) {
        int consumoMin = obtenerConsumoMinimo(mes);
        int consumoMax = obtenerConsumoMaximo(mes);
        String franjas = obtenerConsumoPorFranjas(mes);
        String consumoDias = obtenerConsumoPorDias(mes);

        String mensaje = String.format(
                "Estadísticas de Consumo del mes: %s\n\nConsumo Mínimo: %d kW\nConsumo Máximo: %d kW\n\nConsumo por Franjas:\n%s\n\nConsumo por Días:\n%s",
                mes, consumoMin, consumoMax, franjas, consumoDias
        );

        JOptionPane.showMessageDialog(null, mensaje, "Estadísticas de Consumo", JOptionPane.INFORMATION_MESSAGE);
    }

    private static int obtenerConsumoMinimo(String mes) {
        return switch (mes) {
            case "Enero", "Marzo", "Mayo", "Julio", "Agosto", "Octubre", "Diciembre" -> 100;
            case "Febrero" -> 50;
            default -> 0;
        };
    }

    private static int obtenerConsumoMaximo(String mes) {
        return switch (mes) {
            case "Enero", "Marzo", "Mayo", "Julio", "Agosto", "Octubre", "Diciembre" -> 1000;
            case "Febrero" -> 500;
            default -> 0;
        };
    }

    private static String obtenerConsumoPorFranjas(String mes) {
        return "Franja 1: 100 - 300 kW\nFranja 2: 300 - 600 kW\nFranja 3: 600 - 1000 kW";
    }

    private static String obtenerConsumoPorDias(String mes) {
        return switch (mes) {
            case "Enero" -> "Día 1: 120 kW\nDía 2: 150 kW\n...";
            case "Febrero" -> "Día 1: 90 kW\nDía 2: 110 kW\n...";
            default -> "Datos no disponibles.";
        };
    }

    private static int obtenerNumeroDias(String mes) {
        return switch (mes) {
            case "Enero", "Marzo", "Mayo", "Julio", "Agosto", "Octubre", "Diciembre" -> 31;
            case "Febrero" -> 28;
            case "Abril", "Junio", "Septiembre", "Noviembre" -> 30;
            default -> 0;
        };
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import DAO.BanDAO;
import DAO.HoaDonDao;
import DAO.KhachHangDao;
import DAO.KhoDao;
import DAO.MonAnDao;
import DAO.OrdersDao;
import POJO.Ban;
import POJO.ChiTietHoaDon;
import POJO.KhachHang;
import POJO.MonAn;
import POJO.HoaDon;
import POJO.Kho;
import POJO.Orders;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author quang    
 */
public class DatBan extends javax.swing.JFrame {
    private int currentTableId; // Variable to hold the current table ID
    private JComboBox<Integer> cbbMaKhachHang;
    String loaiMonAn;

    /**
     * Creates new form DatBan
     */
    public DatBan() {
        initComponents();
        loadBanTable();
        loadTrangThaiLabel();
        loadKhachHangComboBox();
        loadLoaiMonAnComboBox();
        loadMonAnComboBox(loaiMonAn);
        refreshOrderTable();
        tblBan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblBan.getTableHeader().setOpaque(false);
        tblBan.getTableHeader().setBackground(new Color(32, 136, 203));
        tblBan.getTableHeader().setForeground(new Color(255, 255, 255));
        tblBan.getRowHeight(25);
       
    }
    
    public void loadBanTable() {
    ArrayList<Ban> listBan = BanDAO.LayDanhSachBan();
    DefaultTableModel model = (DefaultTableModel) tblBan.getModel();
    model.setRowCount(0); // Clear existing rows
    for (Ban ban : listBan) {
        model.addRow(new Object[]{ban.getMaBan(), ban.getTenBan(), ban.getTrangthai()});
    }
}
  public void loadTrangThaiLabel(){
     tblBan.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
    public void valueChanged(ListSelectionEvent event) {
        // Kiểm tra xem đã chọn một dòng mới hay chưa
        if (!event.getValueIsAdjusting()) {
            int selectedRow = tblBan.getSelectedRow();
            if (selectedRow != -1) { // Đảm bảo đã chọn một dòng
                String tenBan = (String) tblBan.getValueAt(selectedRow, 1); // Lấy tên bàn
                String trangThai = (String) tblBan.getValueAt(selectedRow, 2); // Lấy trạng thái bàn
                labelTrangThai.setText("Tên bàn: " + tenBan + ", Trạng thái: " + trangThai);
            }
        }
    }
});
}
 private void capNhatTrangThaiBanDangSuDung(int maBan) {
    BanDAO.capNhatTrangThaiBan(maBan, "Đang sử dụng");
    // Cập nhật lại giao diện hiển thị trạng thái của bàn (nếu cần)
    // Ví dụ: load lại danh sách bàn và hiển thị trạng thái mới của bàn
}

    private void capNhatTrangThaiBanVeTrong(int maBan) {
    BanDAO.capNhatTrangThaiBan(maBan, "Trống");
    // Cập nhật lại giao diện hiển thị trạng thái của bàn (nếu cần)
    // Ví dụ: load lại danh sách bàn và hiển thị trạng thái mới của bàn
}
 
 // Phương thức kiểm tra xem đơn hàng của bàn hiện tại có còn món nào không
private boolean kiemTraDonHangConMon(int maBan) {
    ArrayList<Orders> ordersList = OrdersDao.LayDanhSachOrdersByTableId(maBan);
    return !ordersList.isEmpty();
}

       
   public void loadKhachHangComboBox() {
    ArrayList<Integer> listMaKhachHang = KhachHangDao.layDanhSachMaKhachHang();
    cbbKhachHang.removeAllItems(); // Clear existing items
    for (Integer maKhachHang : listMaKhachHang) {
        cbbKhachHang.addItem(maKhachHang.toString());
    }
}


    
    
    public void loadLoaiMonAnComboBox() {
    ArrayList<MonAn> listLoaiMonAn = MonAnDao.layDanhSachLoaiMonAn();
    cbbLoaiMonAn.removeAllItems(); // Clear existing items
    for (MonAn loaiMonAn : listLoaiMonAn) {
        cbbLoaiMonAn.addItem(loaiMonAn.getLoaiMonAn());
    }
        }
   
    public void loadMonAnComboBox(String loaiMonAn) {
    ArrayList<MonAn> listMonAn = MonAnDao.layDanhSachMonAnTheoLoai(loaiMonAn);
    cbbTenMonAn.removeAllItems(); // Clear existing items
    for (MonAn monAn : listMonAn) {
        cbbTenMonAn.addItem(monAn.getTenMonAn());
    }
}
    
    // Phương thức refreshOrderTable để cập nhật bảng đơn hàng
private void refreshOrderTable() {
    DefaultTableModel model = (DefaultTableModel) tblOrders.getModel();
    model.setRowCount(0); // Xóa các hàng cũ

    // Lấy danh sách đơn hàng theo ID bàn hiện tại
    ArrayList<Orders> ordersList = OrdersDao.LayDanhSachOrdersByTableId(currentTableId);

    for (Orders order : ordersList) {
        model.addRow(new Object[]{
            order.getMaOrder(),
            order.getMaBan(),
            order.getMaKhachHang(),
            order.getMaMonAn(),
            order.getTenMonAn(),
            order.getLoaiMon(),
            order.getGia(),
            order.getTongTien(),
            order.getSoLuong()
        });
    }
}







private void updateTotalPrice() {
    double tongTien = OrdersDao.tinhTongTien(currentTableId);
    // Chuyển đổi tổng tiền sang chuỗi và cập nhật vào JTextField
    txtTongTien.setText(String.format("Tổng tiền: %.2f VND", tongTien));
}



private void resetOrdersTable() {
    DefaultTableModel model = (DefaultTableModel) tblOrders.getModel();
    model.setRowCount(0); // Xóa hết các hàng
    
    // Cập nhật lại tổng tiền
    updateTotalPrice();
}

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        cbbLoaiMonAn = new javax.swing.JComboBox<>();
        cbbTenMonAn = new javax.swing.JComboBox<>();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        btnThemMon = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        btnXoaMon = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBan = new javax.swing.JTable();
        labelTrangThai = new javax.swing.JLabel();
        cbbKhachHang = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblOrders = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnDatBan = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel107 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel113 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnBan = new javax.swing.JLabel();
        btnDatBan1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel108 = new javax.swing.JLabel();
        btnHoaDon = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(190, 215, 220));

        cbbLoaiMonAn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cbbLoaiMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLoaiMonAnActionPerformed(evt);
            }
        });

        jSpinner1.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("ĐẶT BÀN ");

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel2.setText("ĐẶT MÓN");

        btnThanhToan.setBackground(new java.awt.Color(204, 204, 255));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnThanhToan.setText("THANH TOÁN");
        btnThanhToan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnThemMon.setBackground(new java.awt.Color(204, 204, 255));
        btnThemMon.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnThemMon.setText("THÊM MÓN");
        btnThemMon.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnThemMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel3.setText("TỔNG TIỀN");

        txtTongTien.setBackground(new java.awt.Color(204, 204, 204));

        btnXoaMon.setBackground(new java.awt.Color(204, 204, 255));
        btnXoaMon.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnXoaMon.setText("XÓA MÓN");
        btnXoaMon.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnXoaMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMonActionPerformed(evt);
            }
        });

        tblBan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã bàn", "Tên bàn", "Trạng thái"
            }
        ));
        tblBan.setSelectionBackground(new java.awt.Color(255, 51, 51));
        tblBan.getTableHeader().setReorderingAllowed(false);
        tblBan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBanMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblBan);

        labelTrangThai.setText("Tên bàn");

        cbbKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbKhachHangActionPerformed(evt);
            }
        });

        tblOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã orders", "Mã bàn", "Mã khách hàng", "Mã món ăn", "Tên món ăn", "Loại món", "Giá", "Tổng tiền", "Số lượng"
            }
        ));
        jScrollPane4.setViewportView(tblOrders);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        );

        jLabel4.setText("Chọn mã khách hàng");

        jLabel5.setText("Chọn loại món ăn");

        jLabel6.setText("Chọn món ăn");

        jLabel7.setText("Chọn số lượng");

        btnDatBan.setBackground(new java.awt.Color(204, 204, 255));
        btnDatBan.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnDatBan.setText("ĐẶT BÀN");
        btnDatBan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDatBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatBanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDatBan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(labelTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(431, 431, 431))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(84, 84, 84)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGap(3, 3, 3)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel7))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cbbKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(86, 86, 86)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbbTenMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cbbLoaiMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnThemMon, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(31, 31, 31)
                                            .addComponent(btnXoaMon, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(96, 96, 96))))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(92, 92, 92)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap()))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(172, 172, 172))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTrangThai))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbbKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnThemMon, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnXoaMon, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbbLoaiMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbbTenMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2625, 2625, 2625))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDatBan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 774, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        jLabel107.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/images (1).jpg"))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(229, 221, 197));

        jSeparator5.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/bill.png"))); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/booking.png"))); // NOI18N

        jSeparator6.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/human-resources.png"))); // NOI18N

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/money.png"))); // NOI18N

        jSeparator7.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator7.setForeground(new java.awt.Color(255, 255, 255));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/client.png"))); // NOI18N

        jLabel112.setBackground(new java.awt.Color(255, 204, 204));
        jLabel112.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel112.setText("Quản lý kho");
        jLabel112.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel112.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel112MouseClicked(evt);
            }
        });

        jSeparator8.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel113.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel113.setText("Quản lý thực đơn");
        jLabel113.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 1, 1));

        jLabel114.setBackground(new java.awt.Color(255, 204, 204));
        jLabel114.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel114.setText("Quản lý nhân sự");
        jLabel114.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel114.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel114MouseClicked(evt);
            }
        });

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/logout.png"))); // NOI18N

        btnBan.setBackground(new java.awt.Color(255, 204, 204));
        btnBan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnBan.setText("Quản lý bàn");
        btnBan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnBan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBanMouseClicked(evt);
            }
        });

        btnDatBan1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDatBan1.setText("Đặt bàn");
        btnDatBan1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDatBan1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDatBan1MouseClicked(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/restaurant (1).png"))); // NOI18N

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/round-table.png"))); // NOI18N

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator3.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel108.setBackground(new java.awt.Color(255, 204, 204));
        jLabel108.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel108.setText("Thông tin khách hàng");
        jLabel108.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel108.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel108MouseClicked(evt);
            }
        });

        btnHoaDon.setBackground(new java.awt.Color(255, 204, 204));
        btnHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnHoaDon.setText("Thống kê hóa đơn");
        btnHoaDon.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHoaDonMouseClicked(evt);
            }
        });

        jLabel110.setBackground(new java.awt.Color(255, 204, 204));
        jLabel110.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel110.setText("Đăng xuất");
        jLabel110.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel110.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel110jLabel6MouseClicked(evt);
            }
        });

        jLabel111.setBackground(new java.awt.Color(255, 204, 204));
        jLabel111.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel111.setText("Thống kê doanh thu");
        jLabel111.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGE/warehouse.png"))); // NOI18N

        jSeparator4.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator2)
            .addComponent(jSeparator6)
            .addComponent(jSeparator4)
            .addComponent(jSeparator3)
            .addComponent(jSeparator7)
            .addComponent(jSeparator8)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel108)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jSeparator5)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBan, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel112, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDatBan1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel110, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addComponent(btnBan))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel112))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel16))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(btnHoaDon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(btnDatBan1))
                .addGap(5, 5, 5)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel111)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel108)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel110))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addComponent(jLabel107, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel107, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaMonActionPerformed
        // TODO add your handling code here:
       int selectedRow = tblOrders.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một món để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int maOrder = (int) tblOrders.getValueAt(selectedRow, 0);
    
    // Thực hiện xóa món ăn khỏi đơn hàng
    boolean result = OrdersDao.xoaMonAnKhoiDonHang(maOrder);
    if (result) {
        JOptionPane.showMessageDialog(this, "Xóa món thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        // Sau khi xóa thành công, cập nhật lại bảng tblOrders
        refreshOrderTable();
        // Cập nhật lại tổng tiền
        updateTotalPrice();
        
        // Kiểm tra xem đơn hàng còn món nào không
        if (!kiemTraDonHangConMon(currentTableId)) {
            // Nếu đơn hàng không còn món nào, cập nhật trạng thái của bàn về "Trống"
            BanDAO.capNhatTrangThaiBan(currentTableId, "Trống");
            // Cập nhật lại giao diện hiển thị trạng thái của bàn (nếu cần)
                    loadBanTable();

        }
    } else {
        JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xóa món. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnXoaMonActionPerformed

    private void btnThemMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMonActionPerformed
  // Lấy thông tin từ các thành phần giao diện người dùng
    int maBan = currentTableId; // Sử dụng mã bàn đã được chọn từ trước
    int maKhachHang = Integer.parseInt(cbbKhachHang.getSelectedItem().toString()); // Lấy mã khách hàng từ combobox
    String tenMonAn = cbbTenMonAn.getSelectedItem().toString(); // Lấy tên món ăn từ combobox
    String loaiMonAn = cbbLoaiMonAn.getSelectedItem().toString(); // Lấy loại món ăn từ combobox
    int soLuong = (int) jSpinner1.getValue(); // Lấy số lượng từ spinner

    // Kiểm tra tính hợp lệ của dữ liệu
    if (maBan == -1 || maKhachHang == -1 || tenMonAn.isEmpty() || loaiMonAn.isEmpty() || soLuong <= 0) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin món ăn và số lượng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return; // Dừng lại nếu dữ liệu không hợp lệ
    }

    // Lấy mã món ăn từ tên món ăn
    int maMonAn = MonAnDao.layMaMonAnTuTen(tenMonAn);
    if (maMonAn == -1) {
        JOptionPane.showMessageDialog(this, "Không tìm thấy mã món ăn cho " + tenMonAn, "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Kiểm tra xem món ăn đã tồn tại trong đơn hàng hay chưa
    boolean monAnDaTonTai = OrdersDao.kiemTraMonAnTonTaiTrongDonHang(maBan, maMonAn);

    if (monAnDaTonTai) {
        // Nếu món ăn đã tồn tại trong đơn hàng, cập nhật số lượng
        boolean capNhatSoLuong = OrdersDao.capNhatSoLuongMonAnTrongDonHang(maBan, maMonAn, soLuong);

        if (capNhatSoLuong) {
            // Cập nhật hiển thị của bảng đơn hàng và tổng tiền
            refreshOrderTable(); // Cập nhật bảng đơn hàng
            updateTotalPrice(); // Cập nhật tổng tiền
            JOptionPane.showMessageDialog(this, "Cập nhật số lượng món thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi cập nhật số lượng món. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        // Nếu món ăn chưa tồn tại trong đơn hàng, thêm mới món vào đơn hàng
        boolean result = OrdersDao.themMonAnVaoDonHang(maBan, maKhachHang, maMonAn, tenMonAn, loaiMonAn, soLuong);

        if (result) {
                       capNhatTrangThaiBanDangSuDung(maBan);
        loadBanTable();

            // Cập nhật hiển thị của bảng đơn hàng và tổng tiền
            refreshOrderTable(); // Cập nhật bảng đơn hàng
            updateTotalPrice(); // Cập nhật tổng tiền
            JOptionPane.showMessageDialog(this, "Thêm món thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi thêm món. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    }//GEN-LAST:event_btnThemMonActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
   int maBan = currentTableId; // Sử dụng mã bàn hiện tại
    int maKhachHang = Integer.parseInt(cbbKhachHang.getSelectedItem().toString()); // Lấy mã khách hàng từ combobox
    int maNhanVien = 1; // Giả sử bạn có thông tin nhân viên hiện tại

    // Tính tổng tiền của bàn
    double tongTien = OrdersDao.tinhTongTien(maBan);
    
    // Tạo mới hóa đơn
    boolean hoaDonTaoMoi = HoaDonDao.taoMoiHoaDon(maKhachHang, maBan, maNhanVien, tongTien);
    
    if (hoaDonTaoMoi) {
        // Lấy mã hóa đơn vừa tạo
        int maHoaDon = HoaDonDao.layHoaDonMoiNhat();

        // Lấy danh sách orders theo mã bàn
        ArrayList<Orders> ordersList = OrdersDao.LayDanhSachOrdersByTableId(maBan);

        // Thêm chi tiết hóa đơn
        for (Orders order : ordersList) {
            HoaDonDao.themChiTietHoaDon(maHoaDon, order.getMaMonAn(), (int) order.getSoLuong(), order.getGia());
        }

   // Get all items from the Kho table
ArrayList<Kho> khoItems = KhoDao.LayDanhSachKho();

    boolean check = true;
// Update inventory for each item in the Kho table
for (Kho khoItem : khoItems) {
    // Get the total quantity to deduct for the current item
    int quantityToDeduct = 0;
    for (Orders order : ordersList) {
            quantityToDeduct += order.getSoLuong(); // Use int here as SoLuong might be integer
        
    }
    
    // Update inventory using XuatKho (assuming it's modified to handle int)
    if (quantityToDeduct > 0) {
        if (KhoDao.XuatKho(khoItem.getMaNguyenLieu(), quantityToDeduct *1.0)) {
            // Inventory update successful for the item
          
      
        } else {
            // Failed to update inventory for the item
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật kho cho món " + khoItem.getTenNguyenLieu(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            check = false;
            break;
        }
    }
}
        if (check) {
         HoaDonDao.capNhatTrangThaiHoaDon(maHoaDon, "Đã thanh toán");

        // Xóa các đơn hàng liên quan trong bảng Orders (nếu cần)
        OrdersDao.xoaOrdersByTableId(maBan);

        JOptionPane.showMessageDialog(this, "Thanh toán thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        
         capNhatTrangThaiBanVeTrong(currentTableId);
        }
        
              // Cập nhật trạng thái hóa đơn nếu cần
       
    } else {
        JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi thanh toán. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
            
       
        loadBanTable();

    
        resetOrdersTable();
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void cbbLoaiMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLoaiMonAnActionPerformed
        // TODO add your handling code here:
        String selectedLoaiMonAn = (String) cbbLoaiMonAn.getSelectedItem();
        loadMonAnComboBox(selectedLoaiMonAn);
    
    }//GEN-LAST:event_cbbLoaiMonAnActionPerformed

    private void tblBanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBanMouseClicked
         
     int selectedRow = tblBan.getSelectedRow();
    if (selectedRow != -1) {
        int maBan = (int) tblBan.getValueAt(selectedRow, 0);
        // Save selected MaBan to use later
        currentTableId = maBan;
        System.out.println("Selected MaBan: " + maBan);
        refreshOrderTable(); // Refresh order table when a table is selected
    }
        
    }//GEN-LAST:event_tblBanMouseClicked

    private void btnDatBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatBanActionPerformed
        // TODO add your handling code here:
        
         // Lấy chỉ số hàng được chọn trong bảng tblBan
    int selectedRow = tblBan.getSelectedRow();
    
    if (selectedRow != -1) { // Kiểm tra xem có hàng nào được chọn không
        // Lấy mã bàn từ bảng tblBan (giả sử mã bàn ở cột 0)
        int maBan = (int) tblBan.getValueAt(selectedRow, 0);

        // Cập nhật trạng thái bàn trong cơ sở dữ liệu
        boolean capNhatTrangThai = BanDAO.capNhatTrangThaiBan(maBan, "Đang sử dụng");

        if (capNhatTrangThai) {
            // Cập nhật giao diện người dùng
            tblBan.setValueAt("Đang sử dụng", selectedRow, tblBan.getColumn("Trạng thái").getModelIndex());
            JOptionPane.showMessageDialog(this, "Bàn đã được đặt thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi cập nhật trạng thái bàn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn để đặt.", "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_btnDatBanActionPerformed

    private void cbbKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbKhachHangActionPerformed

    private void jLabel112MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel112MouseClicked
        // TODO add your handling code here:
        KhoLayout kho = new KhoLayout();
        kho.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel112MouseClicked

    private void jLabel114MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel114MouseClicked
        // TODO add your handling code here:
        QuanLiNhanSu nhanSu = new QuanLiNhanSu();
        nhanSu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel114MouseClicked

    private void btnBanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBanMouseClicked
        // TODO add your handling code here:
        BanLayout ban = new BanLayout();
        ban.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBanMouseClicked

    private void btnDatBan1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDatBan1MouseClicked
        // TODO add your handling code here:
        DatBan trangChuQL = new DatBan();
        trangChuQL.setVisible(true);
        this.dispose(); // Đóng MainFrame hiện tại
    }//GEN-LAST:event_btnDatBan1MouseClicked

    private void jLabel108MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel108MouseClicked
        // TODO add your handling code here:
        ThongTinKH trangChuQL = new ThongTinKH();
        trangChuQL.setVisible(true);
        this.dispose(); // Đóng MainFrame hiện tại
    }//GEN-LAST:event_jLabel108MouseClicked

    private void btnHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHoaDonMouseClicked
        
    }//GEN-LAST:event_btnHoaDonMouseClicked

    private void jLabel110jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel110jLabel6MouseClicked
        Login login = new Login();
        login.setVisible(true);
        this.dispose(); // Đóng MainFrame hiện tại
    }//GEN-LAST:event_jLabel110jLabel6MouseClicked

    private void loadHoaDonTable(int maBan) {
   
}
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DatBan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DatBan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DatBan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DatBan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DatBan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnBan;
    private javax.swing.JButton btnDatBan;
    private javax.swing.JLabel btnDatBan1;
    private javax.swing.JLabel btnHoaDon;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThemMon;
    private javax.swing.JButton btnXoaMon;
    private javax.swing.JComboBox<String> cbbKhachHang;
    private javax.swing.JComboBox<String> cbbLoaiMonAn;
    private javax.swing.JComboBox<String> cbbTenMonAn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel labelTrangThai;
    private javax.swing.JTable tblBan;
    private javax.swing.JTable tblOrders;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}

-- create database shop;
-- use shop;

-- Vai trò của người dùng: là admin hay end-users
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL
);

-- Người dùng
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,      -- đại diện đăng nhập
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- Token: ví dụ khi đăng nhập thành công thì được cấp 1 cái chìa khóa.
-- Các lần sau có đi đâu làm gì không cần điền lại tài khoản, mật khẩu nữa
CREATE TABLE tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    tokenType VARCHAR(50) NOT NULL,     -- có thể trong khóa học này ta sử dụng JWT token
    expiration_date DATETIME,           -- ngày hết hạn
    revoked TINYINT(1) NOT NULL,        -- thu hồi
    expired TINYINT(1) NOT NULL,        -- hết hạn
    user_id INT,    -- FOREIGN KEY
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Hỗ trợ đăng nhập từ Facebook và Google
CREATE TABLE social_accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(20) NOT NULL COMMENT 'Tên nhà social network',
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT 'Email tài khoản',
    name VARCHAR(100) NOT NULL COMMENT 'Tên người dùng',
    user_id INT,    -- FOREIGN KEY
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Bảng danh mục sản phẩm
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'Tên danh mục, vd: đồ điện tử,... '
);

-- Bảng chứa sản phẩm: "laptop macbook air 15 inch 2023", "iphone 15 pro",...
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) DEFAULT '',
    price FLOAT NOT NULL CHECK (price >= 0),
    thumbnail VARCHAR(300) DEFAULT '',
    description LONGTEXT DEFAULT (''),
    created_at DATETIME,
    updated_at DATETIME,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Bảng chưa ảnh của từng sản phẩm
CREATE TABLE product_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id int,
    FOREIGN KEY (product_id) REFERENCES products(id),
    -- Tạo thêm ràng buộc, nếu thằng Products bị xóa thì các thằng liên quan trong product_images sẽ bị xóa theo
    CONSTRAINT fk_product_images_product_id
        FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    image_url VARCHAR(300)
);

-- Đặt hàng
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    fullname VARCHAR(100),     -- Lý do bảng users đã có fullname rồi mà ở bảng orders vẫn có fullname. Vì tên người ta nhập lúc đặt hàng có thể khác với tên của họ
    email VARCHAR(100),
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) NOT NULL,
    note VARCHAR(100) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') COMMENT 'Trạng thái đơn hàng',  -- Trạng thái giao hàng: nhập kho, đang giao, đã nhận, ...
    total_money FLOAT CHECK (total_money >= 0),
    shipping_method VARCHAR(100),   -- phương thức vận chuyển
    shipping_address VARCHAR(200),  -- địa chỉ nhận hàng
    shipping_date DATE,             -- ngày nhận hàng
    tracking_number VARCHAR(100),   -- số vận đơn, mã vận đơn
    payment_method VARCHAR(100),     -- phương thức thanh toán
    active TINYINT(1)
);

CREATE TABLE order_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    price FLOAT CHECK (price >= 0),
    number_of_products INT CHECK (number_of_products > 0),
    total_money FLOAT CHECK (total_money >= 0),
    color VARCHAR(20) DEFAULT ''
)
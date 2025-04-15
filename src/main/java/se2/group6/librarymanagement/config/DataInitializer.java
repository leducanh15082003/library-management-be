package se2.group6.librarymanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(JdbcTemplate jdbcTemplate) {
        return args -> {
            Integer count = jdbcTemplate.queryForObject("select count(*) from subject", Integer.class);
            if (count != null && count > 0) {
                System.out.println("Data is initialized!");
                return;
            }
            // -------------------- Subject: Sách --------------------
            // Thêm subject "Sách"
            jdbcTemplate.execute("INSERT INTO subject (name, description, image_url) VALUES " +
                    "('Sách', 'Danh mục chứa các tác phẩm văn học', 'https://example.com/sach.jpg')");
            jdbcTemplate.execute("SET @subject_id = LAST_INSERT_ID()");

            // Thêm 3 tác giả cho subject Sách
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Nguyễn Nhật Ánh', 'Nhà văn chuyên viết truyện cho thanh thiếu niên', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Bảo Ninh', 'Nhà văn với tác phẩm nổi bật về chiến tranh', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Nguyễn Ngọc Tư', 'Nhà văn nổi tiếng với truyện ngắn miền Tây', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Lấy ID của các tác giả
            jdbcTemplate.execute("SET @author1 = (SELECT id FROM author WHERE name = 'Nguyễn Nhật Ánh')");
            jdbcTemplate.execute("SET @author2 = (SELECT id FROM author WHERE name = 'Bảo Ninh')");
            jdbcTemplate.execute("SET @author3 = (SELECT id FROM author WHERE name = 'Nguyễn Ngọc Tư')");

            // -------------------- Sách của Nguyễn Nhật Ánh --------------------

            // Chèn sách "Cho tôi xin một vé đi tuổi thơ"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Cho tôi xin một vé đi tuổi thơ', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Nhật Ánh'), " +
                    "    '978-604-1-14824-3', 'Văn học', 'NXB Trẻ', '2008', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742374236/rtwpqi9eritzwyialuua.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Chèn 3 bản sao cho cuốn "Cho tôi xin một vé đi tuổi thơ"
            // Bản sao 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cho tôi xin một vé đi tuổi thơ'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cho tôi xin một vé đi tuổi thơ'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Bản sao 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cho tôi xin một vé đi tuổi thơ'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cho tôi xin một vé đi tuổi thơ'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Bản sao 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cho tôi xin một vé đi tuổi thơ'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Cho tôi xin một vé đi tuổi thơ'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Chèn sách "Cô gái đến từ hôm qua"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Cô gái đến từ hôm qua', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Nhật Ánh'), " +
                    "    '978-604-1-14825-0', 'Văn học', 'NXB Trẻ', '1990', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375399/ba2dt4r1k0uzfmxh9p8r.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Chèn 3 bản sao cho cuốn "Cô gái đến từ hôm qua"
            // Bản sao 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cô gái đến từ hôm qua'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cô gái đến từ hôm qua'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Bản sao 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cô gái đến từ hôm qua'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cô gái đến từ hôm qua'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Bản sao 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cô gái đến từ hôm qua'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Cô gái đến từ hôm qua'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Chèn sách "Mắt biếc"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Mắt biếc', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Nhật Ánh'), " +
                    "    '978-604-1-14826-7', 'Văn học', 'NXB Trẻ', '1990', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375436/z4quio0zyn4pkjm45dl4.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Chèn 3 bản sao cho cuốn "Mắt biếc"
            // Bản sao 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Mắt biếc'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Mắt biếc'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Bản sao 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Mắt biếc'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Mắt biếc'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Bản sao 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Mắt biếc'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Mắt biếc'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Sách của Bảo Ninh --------------------

            // Thêm sách "Nỗi buồn chiến tranh"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Nỗi buồn chiến tranh', " +
                    "    (SELECT id FROM author WHERE name = 'Bảo Ninh'), " +
                    "    '978-604-1-14827-4', 'Chiến tranh', 'NXB Hội Nhà Văn', '1990', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375511/hhnsphhlkxntc1jh30nb.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Nỗi buồn chiến tranh"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Nỗi buồn chiến tranh'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Nỗi buồn chiến tranh'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Nỗi buồn chiến tranh'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Nỗi buồn chiến tranh'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Nỗi buồn chiến tranh'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Nỗi buồn chiến tranh'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm sách "Trại bảy chú lùn"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Trại bảy chú lùn', " +
                    "    (SELECT id FROM author WHERE name = 'Bảo Ninh'), " +
                    "    '978-604-1-14828-1', 'Tiểu thuyết', 'NXB Hội Nhà Văn', '2007', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375538/gxi5n2mdjtknjyc2w0ap.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Trại bảy chú lùn"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Trại bảy chú lùn'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Trại bảy chú lùn'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Trại bảy chú lùn'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Trại bảy chú lùn'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Trại bảy chú lùn'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Trại bảy chú lùn'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm sách "Chim én bay"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Chim én bay', " +
                    "    (SELECT id FROM author WHERE name = 'Bảo Ninh'), " +
                    "    '978-604-1-14829-8', 'Văn học', 'NXB Hội Nhà Văn', '2012', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375609/ljzxjb5l2ljnthzfcauh.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Chim én bay"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Chim én bay'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Chim én bay'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Chim én bay'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Chim én bay'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Chim én bay'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Chim én bay'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Sách của Nguyễn Ngọc Tư --------------------

            // Thêm sách "Cánh đồng bất tận"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Cánh đồng bất tận', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Ngọc Tư'), " +
                    "    '978-604-1-14830-4', 'Truyện ngắn', 'NXB Trẻ', '2005', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375637/jvhd6we3gnav0bxuqcsw.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Cánh đồng bất tận"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cánh đồng bất tận'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cánh đồng bất tận'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cánh đồng bất tận'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cánh đồng bất tận'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Cánh đồng bất tận'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Cánh đồng bất tận'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm sách "Gió lẻ và 9 câu chuyện khác"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Gió lẻ và 9 câu chuyện khác', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Ngọc Tư'), " +
                    "    '978-604-1-14831-1', 'Truyện ngắn', 'NXB Trẻ', '2012', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375678/elngtmwwbwoxdjiefsyq.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Gió lẻ và 9 câu chuyện khác"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Gió lẻ và 9 câu chuyện khác'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Gió lẻ và 9 câu chuyện khác'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Gió lẻ và 9 câu chuyện khác'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Gió lẻ và 9 câu chuyện khác'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Gió lẻ và 9 câu chuyện khác'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Gió lẻ và 9 câu chuyện khác'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm sách "Khói trời lộng lẫy"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Khói trời lộng lẫy', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Ngọc Tư'), " +
                    "    '978-604-1-14832-8', 'Truyện ngắn', 'NXB Trẻ', '2017', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375702/ewtms1izfvlxaxeaozvn.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Khói trời lộng lẫy"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Khói trời lộng lẫy'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Khói trời lộng lẫy'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Khói trời lộng lẫy'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Khói trời lộng lẫy'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('000', (SELECT id FROM book WHERE title = 'Khói trời lộng lẫy'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Khói trời lộng lẫy'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Subject: Tạp chí --------------------
            jdbcTemplate.execute("INSERT INTO subject (name, description, image_url) VALUES " +
                    "('Tạp chí', 'Danh mục chứa các tạp chí nổi bật', 'https://example.com/tapchi.jpg')");
            jdbcTemplate.execute("SET @subject_id = LAST_INSERT_ID()");

            // Thêm 3 tác giả cho subject Tạp chí
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Anna Thompson', 'Renowned journalist specializing in fashion and lifestyle', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Michael Carter', 'Award-winning writer known for investigative journalism', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Sophia Lewis', 'Editor-in-chief with expertise in luxury and travel', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Lấy ID các tác giả cho subject Tạp chí
            jdbcTemplate.execute("SET @author1 = (SELECT id FROM author WHERE name = 'Anna Thompson')");
            jdbcTemplate.execute("SET @author2 = (SELECT id FROM author WHERE name = 'Michael Carter')");
            jdbcTemplate.execute("SET @author3 = (SELECT id FROM author WHERE name = 'Sophia Lewis')");

            // -------------------- Tạp chí: Tạp chí của Anna Thompson --------------------

            // Thêm tạp chí "Vogue"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Vogue', (SELECT id FROM author WHERE name = 'Anna Thompson'), " +
                    "'978-0-12345-678-9', 'Fashion', 'Condé Nast', '2023', " +
                    "@subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742523211/lx6jz8mkr0qkkzr83ndq.jpg', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Vogue" với barcode theo format "010" + book_id + copy number
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Vogue'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Vogue'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Vogue'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Vogue'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Vogue'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Vogue'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm tạp chí "Harper’s Bazaar"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Harper’s Bazaar', " +
                    "(SELECT id FROM author WHERE name = 'Anna Thompson'), " +
                    "'978-0-23456-789-0', 'Fashion', 'Hearst', '2023', " +
                    "@subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742523254/wdt8zhghlg3tk57sbwyy.jpg', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Harper’s Bazaar" với barcode theo format "010" + book_id + copy number
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Harper’s Bazaar'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Harper’s Bazaar'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Harper’s Bazaar'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Harper’s Bazaar'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Harper’s Bazaar'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Harper’s Bazaar'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm tạp chí "Elle"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Elle', " +
                    "    (SELECT id FROM author WHERE name = 'Anna Thompson'), " +
                    "    '978-0-34567-890-1', 'Fashion', 'Lagardère Group', '2023', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742523273/l1cberpsnfvdsunl65mx.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Elle" với barcode theo format "010" + book_id + copy number
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Elle'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Elle'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Elle'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Elle'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Elle'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Elle'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Tạp chí của Michael Carter --------------------

            // Thêm tạp chí "GQ"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('GQ', " +
                    "    (SELECT id FROM author WHERE name = 'Michael Carter'), " +
                    "    '978-0-45678-901-2', 'Men’s Lifestyle', 'Condé Nast', '2023', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742523320/eoiei6scxn6x3crlc6ap.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "GQ"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'GQ'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'GQ'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'GQ'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'GQ'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'GQ'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'GQ'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm tạp chí "Vanity Fair"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Vanity Fair', " +
                    "    (SELECT id FROM author WHERE name = 'Michael Carter'), " +
                    "    '978-0-56789-012-3', 'Celebrity & Culture', 'Condé Nast', '2023', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742587124/lvirfkoadkxicvv1wqoo.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Vanity Fair"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Vanity Fair'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Vanity Fair'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Vanity Fair'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Vanity Fair'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Vanity Fair'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Vanity Fair'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm tạp chí "Esquire"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Esquire', " +
                    "    (SELECT id FROM author WHERE name = 'Michael Carter'), " +
                    "    '978-0-67890-123-4', 'Men’s Fashion & Culture', 'Hearst', '2023', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742523365/qq1ceprtuxvtpe31rgug.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Esquire"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Esquire'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Esquire'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Esquire'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Esquire'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Esquire'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Esquire'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Tạp chí của Sophia Lewis --------------------

            // Thêm tạp chí "Cosmopolitan"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Cosmopolitan', " +
                    "    (SELECT id FROM author WHERE name = 'Sophia Lewis'), " +
                    "    '978-0-78901-234-5', 'Women’s Lifestyle', 'Hearst', '2023', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742523390/pxgnpgw71nvna0frivqc.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");
            // Thêm 3 bản sao cho "Cosmopolitan"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Cosmopolitan'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cosmopolitan'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Cosmopolitan'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cosmopolitan'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Cosmopolitan'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Cosmopolitan'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm tạp chí "Marie Claire"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Marie Claire', " +
                    "    (SELECT id FROM author WHERE name = 'Sophia Lewis'), " +
                    "    '978-0-89012-345-6', 'Women’s Fashion & Beauty', 'Hearst', '2023', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742523417/jcvwe5ht3izyewsnfets.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");
            // Thêm 3 bản sao cho "Marie Claire"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Marie Claire'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Marie Claire'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Marie Claire'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Marie Claire'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'Marie Claire'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Marie Claire'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm tạp chí "InStyle"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('InStyle', " +
                    "    (SELECT id FROM author WHERE name = 'Sophia Lewis'), " +
                    "    '978-0-90123-456-7', 'Fashion & Celebrity', 'Dotdash Meredith', '2023', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742523460/qfh4qecl6ww4hw6r5fo0.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");
            // Thêm 3 bản sao cho "InStyle"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'InStyle'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'InStyle'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'InStyle'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'InStyle'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('010', (SELECT id FROM book WHERE title = 'InStyle'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'InStyle'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Subject: Báo --------------------
            jdbcTemplate.execute("INSERT INTO subject (name, description, image_url) VALUES " +
                    "('Báo', 'Danh mục chứa các bài báo nổi bật', 'https://example.com/bao.jpg')");
            jdbcTemplate.execute("SET @subject_id = LAST_INSERT_ID()");

            // Thêm 3 tác giả cho subject Báo
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Vũ Trọng Phụng', 'Nhà văn, nhà báo nổi tiếng với phong cách phóng sự châm biếm và hiện thực', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Nam Cao', 'Nhà văn hiện thực phê phán nổi bật với những tác phẩm phản ánh xã hội phong kiến', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Nguyễn Tuân', 'Nhà văn với phong cách tùy bút độc đáo, tôn vinh vẻ đẹp của con người và thiên nhiên', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Lấy ID các tác giả cho subject Báo
            jdbcTemplate.execute("SET @author1 = (SELECT id FROM author WHERE name = 'Vũ Trọng Phụng')");
            jdbcTemplate.execute("SET @author2 = (SELECT id FROM author WHERE name = 'Nam Cao')");
            jdbcTemplate.execute("SET @author3 = (SELECT id FROM author WHERE name = 'Nguyễn Tuân')");

            // -------------------- Báo: Bài báo của Vũ Trọng Phụng --------------------

            // Thêm bài báo "Kỹ nghệ lấy Tây"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Kỹ nghệ lấy Tây', " +
                    "    (SELECT id FROM author WHERE name = 'Vũ Trọng Phụng'), " +
                    "    '978-604-1-15000-1', 'Phóng sự', 'NXB Văn Học', '1934', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376207/qm6rrg2yfbwzofwj1hdk.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Kỹ nghệ lấy Tây"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Kỹ nghệ lấy Tây'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Kỹ nghệ lấy Tây'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Kỹ nghệ lấy Tây'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Kỹ nghệ lấy Tây'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Kỹ nghệ lấy Tây'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Kỹ nghệ lấy Tây'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm bài báo "Cơm thầy cơm cô"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Cơm thầy cơm cô', " +
                    "    (SELECT id FROM author WHERE name = 'Vũ Trọng Phụng'), " +
                    "    '978-604-1-15001-8', 'Phóng sự', 'NXB Hội Nhà Văn', '1936', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376233/z7ksz5p3gml5jd7tv5z6.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Cơm thầy cơm cô"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Cơm thầy cơm cô'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cơm thầy cơm cô'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Cơm thầy cơm cô'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Cơm thầy cơm cô'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Cơm thầy cơm cô'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Cơm thầy cơm cô'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm bài báo "Lục xì"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Lục xì', " +
                    "    (SELECT id FROM author WHERE name = 'Vũ Trọng Phụng'), " +
                    "    '978-604-1-15002-5', 'Phóng sự', 'NXB Trẻ', '1937', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376248/u848sbom2zxt1ktqo8u9.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Lục xì"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Lục xì'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Lục xì'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Lục xì'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Lục xì'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Lục xì'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Lục xì'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Báo: Bài báo của Nam Cao --------------------

            // Thêm bài báo "Đời thừa"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Đời thừa', " +
                    "    (SELECT id FROM author WHERE name = 'Nam Cao'), " +
                    "    '978-604-1-15003-2', 'Truyện ngắn', 'NXB Văn Học', '1943', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376267/h0ol2hsrtvxm76eagi41.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Đời thừa"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Đời thừa'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Đời thừa'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Đời thừa'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Đời thừa'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Đời thừa'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Đời thừa'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm bài báo "Sống mòn"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Sống mòn', " +
                    "    (SELECT id FROM author WHERE name = 'Nam Cao'), " +
                    "    '978-604-1-15004-9', 'Tiểu thuyết', 'NXB Hội Nhà Văn', '1944', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376282/zrgk2tyfhjjuvwltr3ec.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Sống mòn"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Sống mòn'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Sống mòn'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Sống mòn'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Sống mòn'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Sống mòn'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Sống mòn'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm bài báo "Chí Phèo"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Chí Phèo', " +
                    "    (SELECT id FROM author WHERE name = 'Nam Cao'), " +
                    "    '978-604-1-15005-6', 'Truyện ngắn', 'NXB Trẻ', '1941', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376318/lothvtfygwttlwne8fxg.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");

            // Thêm 3 bản sao cho "Chí Phèo"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Chí Phèo'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Chí Phèo'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Chí Phèo'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Chí Phèo'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Chí Phèo'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Chí Phèo'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Báo: Bài báo của Nguyễn Tuân --------------------

            // Thêm bài báo "Vang bóng một thời"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Vang bóng một thời', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Tuân'), " +
                    "    '978-604-1-15006-3', 'Tùy bút', 'NXB Văn Học', '1940', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376345/utmljddjdk4syz8itwol.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");
            // Thêm 3 bản sao cho "Vang bóng một thời"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Vang bóng một thời'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Vang bóng một thời'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Vang bóng một thời'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Vang bóng một thời'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Vang bóng một thời'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Vang bóng một thời'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm bài báo "Tùy bút Sông Đà"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Tùy bút Sông Đà', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Tuân'), " +
                    "    '978-604-1-15007-0', 'Tùy bút', 'NXB Hội Nhà Văn', '1960', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376373/v3otrujv5mhlwyghbq1l.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");
            // Thêm 3 bản sao cho "Tùy bút Sông Đà"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Tùy bút Sông Đà'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Tùy bút Sông Đà'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Tùy bút Sông Đà'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Tùy bút Sông Đà'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Tùy bút Sông Đà'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Tùy bút Sông Đà'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");


            // Thêm bài báo "Chiếc lư đồng mắt cua"
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, subject_id, image_url, created_at, updated_at, view_count) VALUES " +
                    "('Chiếc lư đồng mắt cua', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Tuân'), " +
                    "    '978-604-1-15008-7', 'Truyện ngắn', 'NXB Trẻ', '1941', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376391/fcj7eq4zgd4qjphqxcc4.jpg', " +
                    "    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW(), FLOOR(RAND() * 1000))");
            // Thêm 3 bản sao cho "Chiếc lư đồng mắt cua"
            // Copy 1: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Chiếc lư đồng mắt cua'), '1'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Chiếc lư đồng mắt cua'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 2: Available
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Chiếc lư đồng mắt cua'), '2'), " +
                    "'Available', (SELECT id FROM book WHERE title = 'Chiếc lư đồng mắt cua'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            // Copy 3: Borrowed
            jdbcTemplate.execute("INSERT INTO book_copy (barcode, status, book_id, created_at, updated_at) VALUES " +
                    "(CONCAT('020', (SELECT id FROM book WHERE title = 'Chiếc lư đồng mắt cua'), '3'), " +
                    "'Borrowed', (SELECT id FROM book WHERE title = 'Chiếc lư đồng mắt cua'), " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Tạo phòng học (room)

            // tầng 3
            jdbcTemplate.execute("INSERT INTO room (room_name, floor, capacity) VALUES ('Room 301', 3, 4);");
            jdbcTemplate.execute("INSERT INTO room (room_name, floor, capacity) VALUES ('Room 302', 3, 3);");
            jdbcTemplate.execute("INSERT INTO room (room_name, floor, capacity) VALUES ('Room 303', 3, 5);");

            // tầng 4
            jdbcTemplate.execute("INSERT INTO room (room_name, floor, capacity) VALUES ('Room 401', 4, 6);");
            jdbcTemplate.execute("INSERT INTO room (room_name, floor, capacity) VALUES ('Room 402', 4, 3);");
            jdbcTemplate.execute("INSERT INTO room (room_name, floor, capacity) VALUES ('Room 403', 4, 8);");

            // 301
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (1, 1, '08:00:00', '10:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (1, 2, '10:00:00', '12:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (1, 3, '12:00:00', '14:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (1, 4, '14:00:00', '16:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (1, 5, '16:00:00', '18:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (1, 6, '18:00:00', '20:00:00', 'AVAILABLE');");

            // 302
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (2, 1, '08:00:00', '10:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (2, 2, '10:00:00', '12:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (2, 3, '12:00:00', '14:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (2, 4, '14:00:00', '16:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (2, 5, '16:00:00', '18:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (2, 6, '18:00:00', '20:00:00', 'AVAILABLE');");

            // 303
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (3, 1, '08:00:00', '10:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (3, 2, '10:00:00', '12:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (3, 3, '12:00:00', '14:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (3, 4, '14:00:00', '16:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (3, 5, '16:00:00', '18:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (3, 6, '18:00:00', '20:00:00', 'AVAILABLE');");

            // 401
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (4, 1, '08:00:00', '10:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (4, 2, '10:00:00', '12:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (4, 3, '12:00:00', '14:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (4, 4, '14:00:00', '16:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (4, 5, '16:00:00', '18:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (4, 6, '18:00:00', '20:00:00', 'AVAILABLE');");

            // 402
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (5, 1, '08:00:00', '10:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (5, 2, '10:00:00', '12:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (5, 3, '12:00:00', '14:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (5, 4, '14:00:00', '16:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (5, 5, '16:00:00', '18:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (5, 6, '18:00:00', '20:00:00', 'AVAILABLE');");

            // 403
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (6, 1, '08:00:00', '10:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (6, 2, '10:00:00', '12:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (6, 3, '12:00:00', '14:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (6, 4, '14:00:00', '16:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (6, 5, '16:00:00', '18:00:00', 'AVAILABLE');");
            jdbcTemplate.execute("INSERT INTO room_shift (room_id, shift_number, start_time, end_time, status) VALUES (6, 6, '18:00:00', '20:00:00', 'AVAILABLE');");

//            // Tạo người dùng 1 với mã sinh viên kiểu số
//            jdbcTemplate.execute("INSERT INTO user (user_name, password, role, full_name, email, faculty, student_id) VALUES " +
//                    "('user1', 'password123', 0, 'User One', 'user1@example.com', 'Faculty A', '2101040084')");
//
//// Tạo người dùng 2 với mã sinh viên kiểu số
//            jdbcTemplate.execute("INSERT INTO user (user_name, password, role, full_name, email, faculty, student_id) VALUES " +
//                    "('user2', 'password123', 0, 'User Two', 'user2@example.com', 'Faculty B', '2101045678')");
//
//// Tạo người dùng 3 với mã sinh viên kiểu số
//            jdbcTemplate.execute("INSERT INTO user (user_name, password, role, full_name, email, faculty, student_id) VALUES " +
//                    "('user3', 'password123', 0, 'User Three', 'user3@example.com', 'Faculty C', '2101049876')");
//
//// Tạo người dùng 4 với mã sinh viên kiểu số
//            jdbcTemplate.execute("INSERT INTO user (user_name, password, role, full_name, email, faculty, student_id) VALUES " +
//                    "('user4', 'password123', 0, 'User Four', 'user4@example.com', 'Faculty D', '2101045432')");
//
//// Tạo người dùng 5 với mã sinh viên kiểu số
//            jdbcTemplate.execute("INSERT INTO user (user_name, password, role, full_name, email, faculty, student_id) VALUES " +
//                    "('user5', 'password123', 0, 'User Five', 'user5@example.com', 'Faculty E', '2101046543')");
//
//
//
//            System.out.println("5 users have been added to the database!");
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Cho tôi xin một vé đi tuổi thơ'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Cho tôi xin một vé đi tuổi thơ') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user1'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Cô gái đến từ hôm qua'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Cô gái đến từ hôm qua') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user2'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Mắt biếc'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Mắt biếc') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user3'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Nỗi buồn chiến tranh'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Nỗi buồn chiến tranh') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user4'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Trại bảy chú lùn'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Trại bảy chú lùn') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user5'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Chim én bay'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Chim én bay') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user1'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Cánh đồng bất tận'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Cánh đồng bất tận') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user2'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Gió lẻ và 9 câu chuyện khác'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Gió lẻ và 9 câu chuyện khác') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user3'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Khói trời lộng lẫy'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Khói trời lộng lẫy') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user4'))");
//
//            jdbcTemplate.execute("INSERT INTO borrowed_record (borrow_at, return_at, book_id, book_copy_id, user_id) VALUES " +
//                    "(NOW(), NULL, " + // return_at = NULL
//                    "(SELECT id FROM book WHERE title = 'Vogue'), " +
//                    "(SELECT id FROM book_copy WHERE book_id = (SELECT id FROM book WHERE title = 'Vogue') LIMIT 1), " +
//                    "(SELECT id FROM user WHERE user_name = 'user5'))");
//
//
//            System.out.println("10 borrowed records have been added to the database!");

        };
    }
}

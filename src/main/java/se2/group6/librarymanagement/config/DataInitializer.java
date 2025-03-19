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

            // Thêm sách của Nguyễn Nhật Ánh
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Cho tôi xin một vé đi tuổi thơ', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Nhật Ánh'), " +
                    "    '978-604-1-14824-3', 'Văn học', 'NXB Trẻ', '2008', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742374236/rtwpqi9eritzwyialuua.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Cô gái đến từ hôm qua', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Nhật Ánh'), " +
                    "    '978-604-1-14825-0', 'Văn học', 'NXB Trẻ', '1990', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375399/ba2dt4r1k0uzfmxh9p8r.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Mắt biếc', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Nhật Ánh'), " +
                    "    '978-604-1-14826-7', 'Văn học', 'NXB Trẻ', '1990', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375436/z4quio0zyn4pkjm45dl4.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Thêm sách của Bảo Ninh
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Nỗi buồn chiến tranh', " +
                    "    (SELECT id FROM author WHERE name = 'Bảo Ninh'), " +
                    "    '978-604-1-14827-4', 'Chiến tranh', 'NXB Hội Nhà Văn', '1990', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375511/hhnsphhlkxntc1jh30nb.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Trại bảy chú lùn', " +
                    "    (SELECT id FROM author WHERE name = 'Bảo Ninh'), " +
                    "    '978-604-1-14828-1', 'Tiểu thuyết', 'NXB Hội Nhà Văn', '2007', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375538/gxi5n2mdjtknjyc2w0ap.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Chim én bay', " +
                    "    (SELECT id FROM author WHERE name = 'Bảo Ninh'), " +
                    "    '978-604-1-14829-8', 'Văn học', 'NXB Hội Nhà Văn', '2012', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375609/ljzxjb5l2ljnthzfcauh.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Thêm sách của Nguyễn Ngọc Tư
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Cánh đồng bất tận', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Ngọc Tư'), " +
                    "    '978-604-1-14830-4', 'Truyện ngắn', 'NXB Trẻ', '2005', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375637/jvhd6we3gnav0bxuqcsw.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Gió lẻ và 9 câu chuyện khác', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Ngọc Tư'), " +
                    "    '978-604-1-14831-1', 'Truyện ngắn', 'NXB Trẻ', '2012', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375678/elngtmwwbwoxdjiefsyq.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Khói trời lộng lẫy', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Ngọc Tư'), " +
                    "    '978-604-1-14832-8', 'Truyện ngắn', 'NXB Trẻ', '2017', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375702/ewtms1izfvlxaxeaozvn.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // -------------------- Subject: Tạp chí --------------------
            jdbcTemplate.execute("INSERT INTO subject (name, description, image_url) VALUES " +
                    "('Tạp chí', 'Danh mục chứa các tạp chí nổi bật', 'https://example.com/tapchi.jpg')");
            jdbcTemplate.execute("SET @subject_id = LAST_INSERT_ID()");

            // Thêm 3 tác giả cho subject Tạp chí
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Hồ Anh Thái', 'Nhà văn nổi bật với nhiều tiểu thuyết về văn hóa và lịch sử', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Phan Hoàng', 'Nhà thơ và tác giả của nhiều bài viết về thi ca', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO author (name, bio, created_at, updated_at) VALUES " +
                    "('Bùi Văn Tuất', 'Họa sĩ với nhiều tác phẩm nghệ thuật về miền quê', " +
                    "DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Lấy ID các tác giả cho subject Tạp chí
            jdbcTemplate.execute("SET @author1 = (SELECT id FROM author WHERE name = 'Hồ Anh Thái')");
            jdbcTemplate.execute("SET @author2 = (SELECT id FROM author WHERE name = 'Phan Hoàng')");
            jdbcTemplate.execute("SET @author3 = (SELECT id FROM author WHERE name = 'Bùi Văn Tuất')");

            // Thêm tạp chí của Hồ Anh Thái
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Đức Phật, nàng Savitri và tôi', " +
                    "    (SELECT id FROM author WHERE name = 'Hồ Anh Thái'), " +
                    "    '978-604-1-14824-3', 'Văn học', 'NXB Trẻ', '2007', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375740/r7rkrotirjsp9nmnhcww.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Mười lẻ một đêm', " +
                    "    (SELECT id FROM author WHERE name = 'Hồ Anh Thái'), " +
                    "    '978-604-1-14825-0', 'Văn học', 'NXB Hội Nhà Văn', '2012', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375763/ilcuzquw0yqwr9i7yupd.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Người bên này trời bên ấy', " +
                    "    (SELECT id FROM author WHERE name = 'Hồ Anh Thái'), " +
                    "    '978-604-1-14826-7', 'Văn học', 'NXB Văn Học', '2015', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375786/alaka7xbbp3cika25aqj.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Thêm tạp chí của Phan Hoàng
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Thơ 1-2-3: Sự đổi mới trong thi ca Việt', " +
                    "    (SELECT id FROM author WHERE name = 'Phan Hoàng'), " +
                    "    '978-604-1-14827-4', 'Thơ', 'NXB Hội Nhà Văn', '2018', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375916/h5d0fchlgp0slvcmgnqc.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Những góc khuất của cuộc sống qua lăng kính thơ', " +
                    "    (SELECT id FROM author WHERE name = 'Phan Hoàng'), " +
                    "    '978-604-1-14828-1', 'Thơ', 'NXB Trẻ', '2020', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742375989/n6nzgphfbza7optsecjc.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Tình yêu và nhân sinh trong thơ hiện đại', " +
                    "    (SELECT id FROM author WHERE name = 'Phan Hoàng'), " +
                    "    '978-604-1-14829-8', 'Thơ', 'NXB Văn Học', '2021', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376035/ersuuw4hfzemsqelzxet.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Thêm tạp chí của Bùi Văn Tuất
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Trở về tuổi thơ qua nét vẽ', " +
                    "    (SELECT id FROM author WHERE name = 'Bùi Văn Tuất'), " +
                    "    '978-604-1-14830-4', 'Nghệ thuật', 'NXB Mỹ Thuật', '2016', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376085/vqeowlzeoackrvpzm9wm.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Hồn quê trong tranh sơn dầu', " +
                    "    (SELECT id FROM author WHERE name = 'Bùi Văn Tuất'), " +
                    "    '978-604-1-14831-1', 'Nghệ thuật', 'NXB Hội Mỹ Thuật', '2019', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376119/fdxr5jgo2exxohop6str.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Miền ký ức và nghệ thuật đương đại', " +
                    "    (SELECT id FROM author WHERE name = 'Bùi Văn Tuất'), " +
                    "    '978-604-1-14832-8', 'Nghệ thuật', 'NXB Văn Hóa', '2022', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376145/xmeyg75mkatcczgppb7q.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

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

            // Thêm bài báo của Vũ Trọng Phụng
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Kỹ nghệ lấy Tây', " +
                    "    (SELECT id FROM author WHERE name = 'Vũ Trọng Phụng'), " +
                    "    '978-604-1-15000-1', 'Phóng sự', 'NXB Văn Học', '1934', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376207/qm6rrg2yfbwzofwj1hdk.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Cơm thầy cơm cô', " +
                    "    (SELECT id FROM author WHERE name = 'Vũ Trọng Phụng'), " +
                    "    '978-604-1-15001-8', 'Phóng sự', 'NXB Hội Nhà Văn', '1936', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376233/z7ksz5p3gml5jd7tv5z6.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Lục xì', " +
                    "    (SELECT id FROM author WHERE name = 'Vũ Trọng Phụng'), " +
                    "    '978-604-1-15002-5', 'Phóng sự', 'NXB Trẻ', '1937', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376248/u848sbom2zxt1ktqo8u9.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Thêm bài báo của Nam Cao
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Đời thừa', " +
                    "    (SELECT id FROM author WHERE name = 'Nam Cao'), " +
                    "    '978-604-1-15003-2', 'Truyện ngắn', 'NXB Văn Học', '1943', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376267/h0ol2hsrtvxm76eagi41.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Sống mòn', " +
                    "    (SELECT id FROM author WHERE name = 'Nam Cao'), " +
                    "    '978-604-1-15004-9', 'Tiểu thuyết', 'NXB Hội Nhà Văn', '1944', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376282/zrgk2tyfhjjuvwltr3ec.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Chí Phèo', " +
                    "    (SELECT id FROM author WHERE name = 'Nam Cao'), " +
                    "    '978-604-1-15005-6', 'Truyện ngắn', 'NXB Trẻ', '1941', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376318/lothvtfygwttlwne8fxg.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");

            // Thêm bài báo của Nguyễn Tuân
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Vang bóng một thời', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Tuân'), " +
                    "    '978-604-1-15006-3', 'Tùy bút', 'NXB Văn Học', '1940', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376345/utmljddjdk4syz8itwol.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Tùy bút Sông Đà', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Tuân'), " +
                    "    '978-604-1-15007-0', 'Tùy bút', 'NXB Hội Nhà Văn', '1960', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376373/v3otrujv5mhlwyghbq1l.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
            jdbcTemplate.execute("INSERT INTO book (title, author_id, isbn, genre, publisher, published_year, status, subject_id, image_url, created_at, updated_at) VALUES " +
                    "('Chiếc lư đồng mắt cua', " +
                    "    (SELECT id FROM author WHERE name = 'Nguyễn Tuân'), " +
                    "    '978-604-1-15008-7', 'Truyện ngắn', 'NXB Trẻ', '1941', 'Available', " +
                    "    @subject_id, 'https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742376391/fcj7eq4zgd4qjphqxcc4.jpg', DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY), NOW())");
        };
    }
}

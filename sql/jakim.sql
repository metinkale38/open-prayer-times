-- Generated from https://www.e-solat.gov.my/ (zone selector)
-- Zones: 60
--
-- Apply this script to the PostgreSQL database used by the server. It is safe
-- to run repeatedly: existing rows are updated by primary key.

BEGIN;

CREATE TABLE IF NOT EXISTS jakim (
    id varchar(16) PRIMARY KEY,
    lat real,
    lng real,
    country varchar(2) NOT NULL,
    name_tr text[] NOT NULL,
    name_en text[] NOT NULL
);

INSERT INTO jakim (id, lat, lng, country, name_tr, name_en) VALUES
('JHR01', NULL, NULL, 'MY', ARRAY['Johor', 'JHR01 - Pulau Aur dan Pulau Pemanggil'], ARRAY['Johor', 'JHR01 - Pulau Aur dan Pulau Pemanggil']),
('JHR02', NULL, NULL, 'MY', ARRAY['Johor', 'JHR02 - Johor Bahru, Kota Tinggi, Mersing, Kulai'], ARRAY['Johor', 'JHR02 - Johor Bahru, Kota Tinggi, Mersing, Kulai']),
('JHR03', NULL, NULL, 'MY', ARRAY['Johor', 'JHR03 - Kluang, Pontian'], ARRAY['Johor', 'JHR03 - Kluang, Pontian']),
('JHR04', NULL, NULL, 'MY', ARRAY['Johor', 'JHR04 - Batu Pahat, Muar, Segamat, Gemas Johor, Tangkak'], ARRAY['Johor', 'JHR04 - Batu Pahat, Muar, Segamat, Gemas Johor, Tangkak']),
('KDH01', NULL, NULL, 'MY', ARRAY['Kedah', 'KDH01 - Kota Setar, Kubang Pasu, Pokok Sena (Daerah Kecil)'], ARRAY['Kedah', 'KDH01 - Kota Setar, Kubang Pasu, Pokok Sena (Daerah Kecil)']),
('KDH02', NULL, NULL, 'MY', ARRAY['Kedah', 'KDH02 - Kuala Muda, Yan, Pendang'], ARRAY['Kedah', 'KDH02 - Kuala Muda, Yan, Pendang']),
('KDH03', NULL, NULL, 'MY', ARRAY['Kedah', 'KDH03 - Padang Terap, Sik'], ARRAY['Kedah', 'KDH03 - Padang Terap, Sik']),
('KDH04', NULL, NULL, 'MY', ARRAY['Kedah', 'KDH04 - Baling'], ARRAY['Kedah', 'KDH04 - Baling']),
('KDH05', NULL, NULL, 'MY', ARRAY['Kedah', 'KDH05 - Bandar Baharu, Kulim'], ARRAY['Kedah', 'KDH05 - Bandar Baharu, Kulim']),
('KDH06', NULL, NULL, 'MY', ARRAY['Kedah', 'KDH06 - Langkawi'], ARRAY['Kedah', 'KDH06 - Langkawi']),
('KDH07', NULL, NULL, 'MY', ARRAY['Kedah', 'KDH07 - Puncak Gunung Jerai'], ARRAY['Kedah', 'KDH07 - Puncak Gunung Jerai']),
('KTN01', NULL, NULL, 'MY', ARRAY['Kelantan', 'KTN01 - Bachok, Kota Bharu, Machang, Pasir Mas, Pasir Puteh, Tanah Merah, Tumpat, Kuala Krai, Mukim Chiku'], ARRAY['Kelantan', 'KTN01 - Bachok, Kota Bharu, Machang, Pasir Mas, Pasir Puteh, Tanah Merah, Tumpat, Kuala Krai, Mukim Chiku']),
('KTN02', NULL, NULL, 'MY', ARRAY['Kelantan', 'KTN02 - Gua Musang (Daerah Galas Dan Bertam), Jeli, Jajahan Kecil Lojing'], ARRAY['Kelantan', 'KTN02 - Gua Musang (Daerah Galas Dan Bertam), Jeli, Jajahan Kecil Lojing']),
('MLK01', NULL, NULL, 'MY', ARRAY['Melaka', 'MLK01 - SELURUH NEGERI MELAKA'], ARRAY['Melaka', 'MLK01 - SELURUH NEGERI MELAKA']),
('NGS01', NULL, NULL, 'MY', ARRAY['Negeri Sembilan', 'NGS01 - Tampin, Jempol'], ARRAY['Negeri Sembilan', 'NGS01 - Tampin, Jempol']),
('NGS02', NULL, NULL, 'MY', ARRAY['Negeri Sembilan', 'NGS02 - Jelebu, Kuala Pilah, Rembau'], ARRAY['Negeri Sembilan', 'NGS02 - Jelebu, Kuala Pilah, Rembau']),
('NGS03', NULL, NULL, 'MY', ARRAY['Negeri Sembilan', 'NGS03 - Port Dickson, Seremban'], ARRAY['Negeri Sembilan', 'NGS03 - Port Dickson, Seremban']),
('PHG01', NULL, NULL, 'MY', ARRAY['Pahang', 'PHG01 - Pulau Tioman'], ARRAY['Pahang', 'PHG01 - Pulau Tioman']),
('PHG02', NULL, NULL, 'MY', ARRAY['Pahang', 'PHG02 - Kuantan, Pekan, Muadzam Shah'], ARRAY['Pahang', 'PHG02 - Kuantan, Pekan, Muadzam Shah']),
('PHG03', NULL, NULL, 'MY', ARRAY['Pahang', 'PHG03 - Jerantut, Temerloh, Maran, Bera, Chenor, Jengka'], ARRAY['Pahang', 'PHG03 - Jerantut, Temerloh, Maran, Bera, Chenor, Jengka']),
('PHG04', NULL, NULL, 'MY', ARRAY['Pahang', 'PHG04 - Bentong, Lipis, Raub'], ARRAY['Pahang', 'PHG04 - Bentong, Lipis, Raub']),
('PHG05', NULL, NULL, 'MY', ARRAY['Pahang', 'PHG05 - Genting Sempah, Janda Baik, Bukit Tinggi'], ARRAY['Pahang', 'PHG05 - Genting Sempah, Janda Baik, Bukit Tinggi']),
('PHG06', NULL, NULL, 'MY', ARRAY['Pahang', 'PHG06 - Cameron Highlands, Genting Higlands, Bukit Fraser'], ARRAY['Pahang', 'PHG06 - Cameron Highlands, Genting Higlands, Bukit Fraser']),
('PHG07', NULL, NULL, 'MY', ARRAY['Pahang', 'PHG07 - Daerah Rompin, (Mukim Rompin, Mukim Endau, Mukim Pontian)'], ARRAY['Pahang', 'PHG07 - Daerah Rompin, (Mukim Rompin, Mukim Endau, Mukim Pontian)']),
('PLS01', NULL, NULL, 'MY', ARRAY['Perlis', 'PLS01 - Kangar, Padang Besar, Arau'], ARRAY['Perlis', 'PLS01 - Kangar, Padang Besar, Arau']),
('PNG01', NULL, NULL, 'MY', ARRAY['Pulau Pinang', 'PNG01 - Seluruh Negeri Pulau Pinang'], ARRAY['Pulau Pinang', 'PNG01 - Seluruh Negeri Pulau Pinang']),
('PRK01', NULL, NULL, 'MY', ARRAY['Perak', 'PRK01 - Tapah, Slim River, Tanjung Malim'], ARRAY['Perak', 'PRK01 - Tapah, Slim River, Tanjung Malim']),
('PRK02', NULL, NULL, 'MY', ARRAY['Perak', 'PRK02 - Kuala Kangsar, Sg. Siput , Ipoh, Batu Gajah, Kampar'], ARRAY['Perak', 'PRK02 - Kuala Kangsar, Sg. Siput , Ipoh, Batu Gajah, Kampar']),
('PRK03', NULL, NULL, 'MY', ARRAY['Perak', 'PRK03 - Lenggong, Pengkalan Hulu, Grik'], ARRAY['Perak', 'PRK03 - Lenggong, Pengkalan Hulu, Grik']),
('PRK04', NULL, NULL, 'MY', ARRAY['Perak', 'PRK04 - Temengor, Belum'], ARRAY['Perak', 'PRK04 - Temengor, Belum']),
('PRK05', NULL, NULL, 'MY', ARRAY['Perak', 'PRK05 - Kg Gajah, Teluk Intan, Bagan Datuk, Seri Iskandar, Beruas, Parit, Lumut, Sitiawan, Pulau Pangkor'], ARRAY['Perak', 'PRK05 - Kg Gajah, Teluk Intan, Bagan Datuk, Seri Iskandar, Beruas, Parit, Lumut, Sitiawan, Pulau Pangkor']),
('PRK06', NULL, NULL, 'MY', ARRAY['Perak', 'PRK06 - Selama, Taiping, Bagan Serai, Parit Buntar'], ARRAY['Perak', 'PRK06 - Selama, Taiping, Bagan Serai, Parit Buntar']),
('PRK07', NULL, NULL, 'MY', ARRAY['Perak', 'PRK07 - Bukit Larut'], ARRAY['Perak', 'PRK07 - Bukit Larut']),
('SBH01', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH01 - Bahagian Sandakan (Timur), Bukit Garam, Semawang, Temanggong, Tambisan, Bandar Sandakan, Sukau'], ARRAY['Sabah', 'SBH01 - Bahagian Sandakan (Timur), Bukit Garam, Semawang, Temanggong, Tambisan, Bandar Sandakan, Sukau']),
('SBH02', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH02 - Beluran, Telupid, Pinangah, Terusan, Kuamut, Bahagian Sandakan (Barat)'], ARRAY['Sabah', 'SBH02 - Beluran, Telupid, Pinangah, Terusan, Kuamut, Bahagian Sandakan (Barat)']),
('SBH03', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH03 - Lahad Datu, Silabukan, Kunak, Sahabat, Semporna, Tungku, Bahagian Tawau  (Timur)'], ARRAY['Sabah', 'SBH03 - Lahad Datu, Silabukan, Kunak, Sahabat, Semporna, Tungku, Bahagian Tawau  (Timur)']),
('SBH04', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH04 - Bandar Tawau, Balong, Merotai, Kalabakan, Bahagian Tawau (Barat)'], ARRAY['Sabah', 'SBH04 - Bandar Tawau, Balong, Merotai, Kalabakan, Bahagian Tawau (Barat)']),
('SBH05', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH05 - Kudat, Kota Marudu, Pitas, Pulau Banggi, Bahagian Kudat'], ARRAY['Sabah', 'SBH05 - Kudat, Kota Marudu, Pitas, Pulau Banggi, Bahagian Kudat']),
('SBH06', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH06 - Gunung Kinabalu'], ARRAY['Sabah', 'SBH06 - Gunung Kinabalu']),
('SBH07', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH07 - Kota Kinabalu, Ranau, Kota Belud, Tuaran, Penampang, Papar, Putatan, Bahagian Pantai Barat'], ARRAY['Sabah', 'SBH07 - Kota Kinabalu, Ranau, Kota Belud, Tuaran, Penampang, Papar, Putatan, Bahagian Pantai Barat']),
('SBH08', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH08 - Pensiangan, Keningau, Tambunan, Nabawan, Bahagian Pendalaman (Atas)'], ARRAY['Sabah', 'SBH08 - Pensiangan, Keningau, Tambunan, Nabawan, Bahagian Pendalaman (Atas)']),
('SBH09', NULL, NULL, 'MY', ARRAY['Sabah', 'SBH09 - Beaufort, Kuala Penyu, Sipitang, Tenom, Long Pasia, Membakut, Weston, Bahagian Pendalaman (Bawah)'], ARRAY['Sabah', 'SBH09 - Beaufort, Kuala Penyu, Sipitang, Tenom, Long Pasia, Membakut, Weston, Bahagian Pendalaman (Bawah)']),
('SGR01', NULL, NULL, 'MY', ARRAY['Selangor', 'SGR01 - Gombak, Petaling, Sepang, Hulu Langat, Hulu Selangor, S.Alam'], ARRAY['Selangor', 'SGR01 - Gombak, Petaling, Sepang, Hulu Langat, Hulu Selangor, S.Alam']),
('SGR02', NULL, NULL, 'MY', ARRAY['Selangor', 'SGR02 - Kuala Selangor, Sabak Bernam'], ARRAY['Selangor', 'SGR02 - Kuala Selangor, Sabak Bernam']),
('SGR03', NULL, NULL, 'MY', ARRAY['Selangor', 'SGR03 - Klang, Kuala Langat'], ARRAY['Selangor', 'SGR03 - Klang, Kuala Langat']),
('SWK01', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK01 - Limbang, Lawas, Sundar, Trusan'], ARRAY['Sarawak', 'SWK01 - Limbang, Lawas, Sundar, Trusan']),
('SWK02', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK02 - Miri, Niah, Bekenu, Sibuti, Marudi'], ARRAY['Sarawak', 'SWK02 - Miri, Niah, Bekenu, Sibuti, Marudi']),
('SWK03', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK03 - Pandan, Belaga, Suai, Tatau, Sebauh, Bintulu'], ARRAY['Sarawak', 'SWK03 - Pandan, Belaga, Suai, Tatau, Sebauh, Bintulu']),
('SWK04', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK04 - Sibu, Mukah, Dalat, Song, Igan, Oya, Balingian, Kanowit, Kapit'], ARRAY['Sarawak', 'SWK04 - Sibu, Mukah, Dalat, Song, Igan, Oya, Balingian, Kanowit, Kapit']),
('SWK05', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK05 - Sarikei, Matu, Julau, Rajang, Daro, Bintangor, Belawai'], ARRAY['Sarawak', 'SWK05 - Sarikei, Matu, Julau, Rajang, Daro, Bintangor, Belawai']),
('SWK06', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK06 - Lubok Antu, Sri Aman, Roban, Debak, Kabong, Lingga, Engkelili, Betong, Spaoh, Pusa, Saratok'], ARRAY['Sarawak', 'SWK06 - Lubok Antu, Sri Aman, Roban, Debak, Kabong, Lingga, Engkelili, Betong, Spaoh, Pusa, Saratok']),
('SWK07', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK07 - Serian, Simunjan, Samarahan, Sebuyau, Meludam'], ARRAY['Sarawak', 'SWK07 - Serian, Simunjan, Samarahan, Sebuyau, Meludam']),
('SWK08', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK08 - Kuching, Bau, Lundu, Sematan'], ARRAY['Sarawak', 'SWK08 - Kuching, Bau, Lundu, Sematan']),
('SWK09', NULL, NULL, 'MY', ARRAY['Sarawak', 'SWK09 - Zon Khas (Kampung Patarikan)'], ARRAY['Sarawak', 'SWK09 - Zon Khas (Kampung Patarikan)']),
('TRG01', NULL, NULL, 'MY', ARRAY['Terengganu', 'TRG01 - Kuala Terengganu, Marang, Kuala Nerus'], ARRAY['Terengganu', 'TRG01 - Kuala Terengganu, Marang, Kuala Nerus']),
('TRG02', NULL, NULL, 'MY', ARRAY['Terengganu', 'TRG02 - Besut, Setiu'], ARRAY['Terengganu', 'TRG02 - Besut, Setiu']),
('TRG03', NULL, NULL, 'MY', ARRAY['Terengganu', 'TRG03 - Hulu Terengganu'], ARRAY['Terengganu', 'TRG03 - Hulu Terengganu']),
('TRG04', NULL, NULL, 'MY', ARRAY['Terengganu', 'TRG04 - Dungun, Kemaman'], ARRAY['Terengganu', 'TRG04 - Dungun, Kemaman']),
('WLY01', NULL, NULL, 'MY', ARRAY['Wilayah Persekutuan', 'WLY01 - Kuala Lumpur, Putrajaya'], ARRAY['Wilayah Persekutuan', 'WLY01 - Kuala Lumpur, Putrajaya']),
('WLY02', NULL, NULL, 'MY', ARRAY['Wilayah Persekutuan', 'WLY02 - Labuan'], ARRAY['Wilayah Persekutuan', 'WLY02 - Labuan'])
ON CONFLICT (id) DO UPDATE SET
    lat = EXCLUDED.lat,
    lng = EXCLUDED.lng,
    country = EXCLUDED.country,
    name_tr = EXCLUDED.name_tr,
    name_en = EXCLUDED.name_en;

COMMIT;

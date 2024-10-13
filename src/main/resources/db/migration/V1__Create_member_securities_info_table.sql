DROP TABLE IF EXISTS member_securities_info;
CREATE TABLE member_securities_info (
    member_securities_info_id BIGSERIAL NOT NULL,
    member_id UUID NOT NULL,
    securities_code VARCHAR(3) NOT NULL,
    app_key TEXT NOT NULL,
    app_secret_value TEXT NOT NULL,
    access_token TEXT NOT NULL,
    access_token_expires_in TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (member_securities_info_id)
);

COMMENT ON COLUMN member_securities_info.member_id IS '멤버ID';
COMMENT ON COLUMN member_securities_info.securities_code IS '증권사코드';
COMMENT ON COLUMN member_securities_info.app_key IS '앱키';
COMMENT ON COLUMN member_securities_info.app_secret_value IS '앱시크릿값';
COMMENT ON COLUMN member_securities_info.access_token IS '액세스토큰';
COMMENT ON COLUMN member_securities_info.access_token_expires_in IS '액세스토큰만료시간';
COMMENT ON COLUMN member_securities_info.created_at IS '생성시간';
COMMENT ON COLUMN member_securities_info.updated_at IS '수정시간';
COMMENT ON COLUMN member_securities_info.created_by IS '생성자';
COMMENT ON COLUMN member_securities_info.updated_by IS '수정자';

DROP TABLE IF EXISTS securities_account_info;
CREATE TABLE securities_account_info (
    securities_account_info_id UUID NOT NULL,
    member_id UUID NOT NULL,
    securities_code VARCHAR(3) NOT NULL,
    encrypted_account_number VARCHAR(1000) NOT NULL,
    account_type_code VARCHAR(100) NOT NULL,
    inquiry_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (securities_account_info_id)
);

COMMENT ON COLUMN securities_account_info.securities_account_info_id IS '증권계좌정보ID';
COMMENT ON COLUMN securities_account_info.member_id IS '멤버ID';
COMMENT ON COLUMN securities_account_info.securities_code IS '증권사코드';
COMMENT ON COLUMN securities_account_info.encrypted_account_number IS '암호화된계좌번호';
COMMENT ON COLUMN securities_account_info.account_type_code IS '계좌유형코드';
COMMENT ON COLUMN securities_account_info.inquiry_timestamp IS '조회일시';
COMMENT ON COLUMN securities_account_info.balance IS '잔액';
COMMENT ON COLUMN securities_account_info.created_at IS '생성시간';
COMMENT ON COLUMN securities_account_info.updated_at IS '수정시간';
COMMENT ON COLUMN securities_account_info.created_by IS '생성자';
COMMENT ON COLUMN securities_account_info.updated_by IS '수정자';

DROP TABLE IF EXISTS stock_item_info;
CREATE TABLE stock_item_info (
    stock_item_info_id UUID NOT NULL,
    securities_account_info_id UUID NOT NULL,
    stock_code VARCHAR(6) NOT NULL,
    stock_name VARCHAR(100) NOT NULL,
    stock_quantity DECIMAL(15, 2) NOT NULL,
    stock_price DECIMAL(15, 2) NOT NULL,
    stock_total_price DECIMAL(15, 2) NOT NULL,
    reference_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (stock_item_info_id)
);

COMMENT ON COLUMN stock_item_info.stock_item_info_id IS '주식항목정보ID';
COMMENT ON COLUMN stock_item_info.securities_account_info_id IS '증권계좌정보ID';
COMMENT ON COLUMN stock_item_info.stock_code IS '주식코드';
COMMENT ON COLUMN stock_item_info.stock_name IS '주식명';
COMMENT ON COLUMN stock_item_info.stock_quantity IS '주식수량';
COMMENT ON COLUMN stock_item_info.stock_price IS '주식가격';
COMMENT ON COLUMN stock_item_info.stock_total_price IS '주식총가격';
COMMENT ON COLUMN stock_item_info.reference_timestamp IS '기준일시';
COMMENT ON COLUMN stock_item_info.created_at IS '생성시간';
COMMENT ON COLUMN stock_item_info.updated_at IS '수정시간';
COMMENT ON COLUMN stock_item_info.created_by IS '생성자';
COMMENT ON COLUMN stock_item_info.updated_by IS '수정자';
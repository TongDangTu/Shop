export interface OrderResponse {
    id: number,
    user_id: number,
    fullname: string,
    email: string,
    phone_number: string,
    address: string,
    note: string,
    order_date: string,
    status: string,
    total_money: number,
    shipping_method: string,
    shipping_address: string,
    shipping_date: string,
    tracking_number: string,
    payment_method: string,
    active: boolean,
}

export interface OrderDetailAndProductThumbnailResponse {
    id: number,
    order_id: number,
    product_id: number,
    price: number,
    number_of_products: number,
    total_money: number,
    name: string,
    thumbnail: string,
}
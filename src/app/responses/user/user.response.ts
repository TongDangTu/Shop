export interface UserResponse {
    id: number;
    address: string;
    fullname: string;
    phone_number: string;
    is_active: boolean;
    date_of_birth: Date|number;
    facebook_account_id: number;
    goole_account_id: number;
    role_name: string;
}
import { BaseEntity } from './../../shared';

export const enum OrderStatus {
    'NOPAY',
    'PAYED'
}

export class VipOrder implements BaseEntity {
    constructor(
        public id?: number,
        public createTime?: any,
        public paymentTime?: any,
        public totalPrice?: number,
        public months?: number,
        public transactionId?: string,
        public outTradeNo?: string,
        public tradeType?: string,
        public paymentResult?: any,
        public status?: OrderStatus,
        public openId?: string,
        public userLogin?: string,
        public userId?: number,
    ) {
    }
}

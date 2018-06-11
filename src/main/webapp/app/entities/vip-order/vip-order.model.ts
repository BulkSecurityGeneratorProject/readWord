import { BaseEntity } from './../../shared';

export const enum VipOrderStatus {
    'NOPAY',
    'PAYED',
    'CLOSED'
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
        public status?: VipOrderStatus,
        public openId?: string,
        public productName?: string,
        public productId?: number,
        public userLogin?: string,
        public userId?: number,
    ) {
    }
}

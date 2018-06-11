import { BaseEntity } from './../../shared';

export const enum LifeStatus {
    'DELETE',
    'AVAILABLE'
}

export class Product implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public price?: number,
        public totalMonths?: number,
        public rank?: number,
        public desctription?: any,
        public lifeStatus?: LifeStatus,
    ) {
    }
}

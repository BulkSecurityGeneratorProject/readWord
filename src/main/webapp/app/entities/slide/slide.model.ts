import { BaseEntity } from './../../shared';

export const enum LifeStatus {
    'DELETE',
    'AVAILABLE'
}

export class Slide implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public rank?: number,
        public lifeStatus?: LifeStatus,
        public imgName?: string,
        public imgId?: number,
    ) {
    }
}

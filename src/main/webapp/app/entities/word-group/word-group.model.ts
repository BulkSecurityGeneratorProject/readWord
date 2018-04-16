import { BaseEntity } from './../../shared';

export class WordGroup implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public rank?: number,
        public imgName?: string,
        public imgId?: number,
        public userLogin?: string,
        public userId?: number,
    ) {
    }
}

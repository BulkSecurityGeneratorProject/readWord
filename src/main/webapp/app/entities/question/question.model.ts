import { BaseEntity } from './../../shared';

export class Question implements BaseEntity {
    constructor(
        public id?: number,
        public contact?: string,
        public desctription?: any,
        public userLogin?: string,
        public userId?: number,
    ) {
    }
}

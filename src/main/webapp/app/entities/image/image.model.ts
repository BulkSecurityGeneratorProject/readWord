import { BaseEntity } from './../../shared';

export class Image implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public url?: string,
        public contentContentType?: string,
        public content?: any,
    ) {
    }
}

import { BaseEntity } from './../../shared';

export class Message implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public sendTime?: any,
        public imgName?: string,
        public imgId?: number,
        public contentName?: string,
        public contentId?: number,
    ) {
    }
}

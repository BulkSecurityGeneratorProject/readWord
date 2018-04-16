import { BaseEntity } from './../../shared';

export class MessageContent implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public content?: any,
    ) {
    }
}

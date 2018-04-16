import { BaseEntity } from './../../shared';

export const enum MessageStatusEnum {
    'READ',
    'DELETE'
}

export class MessageStatus implements BaseEntity {
    constructor(
        public id?: number,
        public status?: MessageStatusEnum,
        public msgName?: string,
        public msgId?: number,
        public userLogin?: string,
        public userId?: number,
    ) {
    }
}

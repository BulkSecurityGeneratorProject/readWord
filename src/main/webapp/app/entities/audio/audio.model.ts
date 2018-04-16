import { BaseEntity } from './../../shared';

export class Audio implements BaseEntity {
    constructor(
        public id?: number,
        public url?: string,
        public name?: string,
    ) {
    }
}

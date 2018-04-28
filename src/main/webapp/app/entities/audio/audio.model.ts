import { BaseEntity } from './../../shared';

export class Audio implements BaseEntity {
    constructor(
        public id?: number,
        public url?: string,
        public oneSpeedUrl?: string,
        public name?: string,
    ) {
    }
}

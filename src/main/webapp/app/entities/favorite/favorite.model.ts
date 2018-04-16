import { BaseEntity } from './../../shared';

export class Favorite implements BaseEntity {
    constructor(
        public id?: number,
        public userLogin?: string,
        public userId?: number,
        public words?: BaseEntity[],
    ) {
    }
}

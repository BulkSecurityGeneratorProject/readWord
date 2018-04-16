import { BaseEntity } from './../../shared';

export class Word implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public rank?: number,
        public desctription?: any,
        public imgName?: string,
        public imgId?: number,
        public audioName?: string,
        public audioId?: number,
        public userLogin?: string,
        public userId?: number,
        public wordGroupName?: string,
        public wordGroupId?: number,
        public favorites?: BaseEntity[],
    ) {
    }
}

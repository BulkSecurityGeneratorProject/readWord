import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ReadWordWordModule } from './word/word.module';
import { ReadWordFavoriteModule } from './favorite/favorite.module';
import { ReadWordWordGroupModule } from './word-group/word-group.module';
import { ReadWordImageModule } from './image/image.module';
import { ReadWordAudioModule } from './audio/audio.module';
import { ReadWordMessageModule } from './message/message.module';
import { ReadWordMessageContentModule } from './message-content/message-content.module';
import { ReadWordMessageStatusModule } from './message-status/message-status.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ReadWordWordModule,
        ReadWordFavoriteModule,
        ReadWordWordGroupModule,
        ReadWordImageModule,
        ReadWordAudioModule,
        ReadWordMessageModule,
        ReadWordMessageContentModule,
        ReadWordMessageStatusModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReadWordEntityModule {}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReBaseSharedModule } from 'app/shared/shared.module';
import { AlienComponent } from './alien.component';
import { AlienDetailComponent } from './alien-detail.component';
import { AlienUpdateComponent } from './alien-update.component';
import { AlienDeletePopupComponent, AlienDeleteDialogComponent } from './alien-delete-dialog.component';
import { alienRoute, alienPopupRoute } from './alien.route';

const ENTITY_STATES = [...alienRoute, ...alienPopupRoute];

@NgModule({
  imports: [ReBaseSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [AlienComponent, AlienDetailComponent, AlienUpdateComponent, AlienDeleteDialogComponent, AlienDeletePopupComponent],
  entryComponents: [AlienDeleteDialogComponent]
})
export class ReBaseAlienModule {}

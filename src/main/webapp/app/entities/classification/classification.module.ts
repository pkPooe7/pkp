import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReBaseSharedModule } from 'app/shared/shared.module';
import { ClassificationComponent } from './classification.component';
import { ClassificationDetailComponent } from './classification-detail.component';
import { ClassificationUpdateComponent } from './classification-update.component';
import { ClassificationDeletePopupComponent, ClassificationDeleteDialogComponent } from './classification-delete-dialog.component';
import { classificationRoute, classificationPopupRoute } from './classification.route';

const ENTITY_STATES = [...classificationRoute, ...classificationPopupRoute];

@NgModule({
  imports: [ReBaseSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ClassificationComponent,
    ClassificationDetailComponent,
    ClassificationUpdateComponent,
    ClassificationDeleteDialogComponent,
    ClassificationDeletePopupComponent
  ],
  entryComponents: [ClassificationDeleteDialogComponent]
})
export class ReBaseClassificationModule {}

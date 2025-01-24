import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDistribuidora } from '../distribuidora.model';
import { DistribuidoraService } from '../service/distribuidora.service';

const distribuidoraResolve = (route: ActivatedRouteSnapshot): Observable<null | IDistribuidora> => {
  const id = route.params.id;
  if (id) {
    return inject(DistribuidoraService)
      .find(id)
      .pipe(
        mergeMap((distribuidora: HttpResponse<IDistribuidora>) => {
          if (distribuidora.body) {
            return of(distribuidora.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default distribuidoraResolve;

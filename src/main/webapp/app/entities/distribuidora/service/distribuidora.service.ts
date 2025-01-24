import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDistribuidora, NewDistribuidora } from '../distribuidora.model';

export type PartialUpdateDistribuidora = Partial<IDistribuidora> & Pick<IDistribuidora, 'id'>;

export type EntityResponseType = HttpResponse<IDistribuidora>;
export type EntityArrayResponseType = HttpResponse<IDistribuidora[]>;

@Injectable({ providedIn: 'root' })
export class DistribuidoraService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/distribuidoras');

  create(distribuidora: NewDistribuidora): Observable<EntityResponseType> {
    return this.http.post<IDistribuidora>(this.resourceUrl, distribuidora, { observe: 'response' });
  }

  update(distribuidora: IDistribuidora): Observable<EntityResponseType> {
    return this.http.put<IDistribuidora>(`${this.resourceUrl}/${this.getDistribuidoraIdentifier(distribuidora)}`, distribuidora, {
      observe: 'response',
    });
  }

  partialUpdate(distribuidora: PartialUpdateDistribuidora): Observable<EntityResponseType> {
    return this.http.patch<IDistribuidora>(`${this.resourceUrl}/${this.getDistribuidoraIdentifier(distribuidora)}`, distribuidora, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDistribuidora>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDistribuidora[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDistribuidoraIdentifier(distribuidora: Pick<IDistribuidora, 'id'>): number {
    return distribuidora.id;
  }

  compareDistribuidora(o1: Pick<IDistribuidora, 'id'> | null, o2: Pick<IDistribuidora, 'id'> | null): boolean {
    return o1 && o2 ? this.getDistribuidoraIdentifier(o1) === this.getDistribuidoraIdentifier(o2) : o1 === o2;
  }

  addDistribuidoraToCollectionIfMissing<Type extends Pick<IDistribuidora, 'id'>>(
    distribuidoraCollection: Type[],
    ...distribuidorasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const distribuidoras: Type[] = distribuidorasToCheck.filter(isPresent);
    if (distribuidoras.length > 0) {
      const distribuidoraCollectionIdentifiers = distribuidoraCollection.map(distribuidoraItem =>
        this.getDistribuidoraIdentifier(distribuidoraItem),
      );
      const distribuidorasToAdd = distribuidoras.filter(distribuidoraItem => {
        const distribuidoraIdentifier = this.getDistribuidoraIdentifier(distribuidoraItem);
        if (distribuidoraCollectionIdentifiers.includes(distribuidoraIdentifier)) {
          return false;
        }
        distribuidoraCollectionIdentifiers.push(distribuidoraIdentifier);
        return true;
      });
      return [...distribuidorasToAdd, ...distribuidoraCollection];
    }
    return distribuidoraCollection;
  }
}

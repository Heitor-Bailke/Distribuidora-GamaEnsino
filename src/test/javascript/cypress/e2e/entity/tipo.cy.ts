import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Tipo e2e test', () => {
  const tipoPageUrl = '/tipo';
  const tipoPageUrlPattern = new RegExp('/tipo(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tipoSample = { name: 'whoever acknowledge' };

  let tipo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipos/${tipo.id}`,
      }).then(() => {
        tipo = undefined;
      });
    }
  });

  it('Tipos menu should load Tipos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Tipo').should('exist');
    cy.url().should('match', tipoPageUrlPattern);
  });

  describe('Tipo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Tipo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tipo/new$'));
        cy.getEntityCreateUpdateHeading('Tipo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipos',
          body: tipoSample,
        }).then(({ body }) => {
          tipo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tipos?page=0&size=20>; rel="last",<http://localhost/api/tipos?page=0&size=20>; rel="first"',
              },
              body: [tipo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Tipo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoPageUrlPattern);
      });

      it('edit button click should load edit Tipo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tipo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoPageUrlPattern);
      });

      it('edit button click should load edit Tipo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tipo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoPageUrlPattern);
      });

      it('last delete button click should delete instance of Tipo', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoPageUrlPattern);

        tipo = undefined;
      });
    });
  });

  describe('new Tipo page', () => {
    beforeEach(() => {
      cy.visit(`${tipoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Tipo');
    });

    it('should create an instance of Tipo', () => {
      cy.get(`[data-cy="name"]`).type('provided ack');
      cy.get(`[data-cy="name"]`).should('have.value', 'provided ack');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        tipo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoPageUrlPattern);
    });
  });
});

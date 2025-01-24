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

describe('Distribuidora e2e test', () => {
  const distribuidoraPageUrl = '/distribuidora';
  const distribuidoraPageUrlPattern = new RegExp('/distribuidora(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const distribuidoraSample = {
    nome: 'indeed',
    cep: 'slipper ',
    cidade: 'regal',
    bairro: 'even',
    rua: 'ha awesome chunter',
    numero: 1708,
    estado: 'th',
  };

  let distribuidora;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/distribuidoras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/distribuidoras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/distribuidoras/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (distribuidora) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/distribuidoras/${distribuidora.id}`,
      }).then(() => {
        distribuidora = undefined;
      });
    }
  });

  it('Distribuidoras menu should load Distribuidoras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('distribuidora');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Distribuidora').should('exist');
    cy.url().should('match', distribuidoraPageUrlPattern);
  });

  describe('Distribuidora page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(distribuidoraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Distribuidora page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/distribuidora/new$'));
        cy.getEntityCreateUpdateHeading('Distribuidora');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distribuidoraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/distribuidoras',
          body: distribuidoraSample,
        }).then(({ body }) => {
          distribuidora = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/distribuidoras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [distribuidora],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(distribuidoraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Distribuidora page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('distribuidora');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distribuidoraPageUrlPattern);
      });

      it('edit button click should load edit Distribuidora page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Distribuidora');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distribuidoraPageUrlPattern);
      });

      it('edit button click should load edit Distribuidora page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Distribuidora');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distribuidoraPageUrlPattern);
      });

      it('last delete button click should delete instance of Distribuidora', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('distribuidora').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distribuidoraPageUrlPattern);

        distribuidora = undefined;
      });
    });
  });

  describe('new Distribuidora page', () => {
    beforeEach(() => {
      cy.visit(`${distribuidoraPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Distribuidora');
    });

    it('should create an instance of Distribuidora', () => {
      cy.get(`[data-cy="nome"]`).type('engage');
      cy.get(`[data-cy="nome"]`).should('have.value', 'engage');

      cy.get(`[data-cy="cep"]`).type('owlishly');
      cy.get(`[data-cy="cep"]`).should('have.value', 'owlishly');

      cy.get(`[data-cy="cidade"]`).type('cycle');
      cy.get(`[data-cy="cidade"]`).should('have.value', 'cycle');

      cy.get(`[data-cy="bairro"]`).type('rationalise desk');
      cy.get(`[data-cy="bairro"]`).should('have.value', 'rationalise desk');

      cy.get(`[data-cy="rua"]`).type('structure');
      cy.get(`[data-cy="rua"]`).should('have.value', 'structure');

      cy.get(`[data-cy="numero"]`).type('19369');
      cy.get(`[data-cy="numero"]`).should('have.value', '19369');

      cy.get(`[data-cy="referencia"]`).type('concrete');
      cy.get(`[data-cy="referencia"]`).should('have.value', 'concrete');

      cy.get(`[data-cy="estado"]`).type('ha');
      cy.get(`[data-cy="estado"]`).should('have.value', 'ha');

      cy.get(`[data-cy="detalhes"]`).type('boldly under lobotomise');
      cy.get(`[data-cy="detalhes"]`).should('have.value', 'boldly under lobotomise');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        distribuidora = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', distribuidoraPageUrlPattern);
    });
  });
});

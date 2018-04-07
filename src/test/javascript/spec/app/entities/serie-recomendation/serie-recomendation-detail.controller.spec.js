'use strict';

describe('Controller Tests', function() {

    describe('SerieRecomendation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSerieRecomendation, MockSerie, MockRequest, MockPreferences;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSerieRecomendation = jasmine.createSpy('MockSerieRecomendation');
            MockSerie = jasmine.createSpy('MockSerie');
            MockRequest = jasmine.createSpy('MockRequest');
            MockPreferences = jasmine.createSpy('MockPreferences');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SerieRecomendation': MockSerieRecomendation,
                'Serie': MockSerie,
                'Request': MockRequest,
                'Preferences': MockPreferences
            };
            createController = function() {
                $injector.get('$controller')("SerieRecomendationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'serflixApp:serieRecomendationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

'use strict';

describe('Controller Tests', function() {

    describe('Request Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRequest, MockUser, MockLocation, MockForecast, MockMovieRecomendation, MockSerieRecomendation, MockPreferences;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRequest = jasmine.createSpy('MockRequest');
            MockUser = jasmine.createSpy('MockUser');
            MockLocation = jasmine.createSpy('MockLocation');
            MockForecast = jasmine.createSpy('MockForecast');
            MockMovieRecomendation = jasmine.createSpy('MockMovieRecomendation');
            MockSerieRecomendation = jasmine.createSpy('MockSerieRecomendation');
            MockPreferences = jasmine.createSpy('MockPreferences');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Request': MockRequest,
                'User': MockUser,
                'Location': MockLocation,
                'Forecast': MockForecast,
                'MovieRecomendation': MockMovieRecomendation,
                'SerieRecomendation': MockSerieRecomendation,
                'Preferences': MockPreferences
            };
            createController = function() {
                $injector.get('$controller')("RequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'serflixApp:requestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

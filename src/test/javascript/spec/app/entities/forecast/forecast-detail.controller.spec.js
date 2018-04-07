'use strict';

describe('Controller Tests', function() {

    describe('Forecast Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockForecast, MockRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockForecast = jasmine.createSpy('MockForecast');
            MockRequest = jasmine.createSpy('MockRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Forecast': MockForecast,
                'Request': MockRequest
            };
            createController = function() {
                $injector.get('$controller')("ForecastDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'serflixApp:forecastUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

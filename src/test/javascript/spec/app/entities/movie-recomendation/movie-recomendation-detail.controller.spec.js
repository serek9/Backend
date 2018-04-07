'use strict';

describe('Controller Tests', function() {

    describe('MovieRecomendation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMovieRecomendation, MockMovie, MockRequest, MockPreferences;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMovieRecomendation = jasmine.createSpy('MockMovieRecomendation');
            MockMovie = jasmine.createSpy('MockMovie');
            MockRequest = jasmine.createSpy('MockRequest');
            MockPreferences = jasmine.createSpy('MockPreferences');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MovieRecomendation': MockMovieRecomendation,
                'Movie': MockMovie,
                'Request': MockRequest,
                'Preferences': MockPreferences
            };
            createController = function() {
                $injector.get('$controller')("MovieRecomendationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'serflixApp:movieRecomendationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

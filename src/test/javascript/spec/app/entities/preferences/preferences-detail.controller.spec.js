'use strict';

describe('Controller Tests', function() {

    describe('Preferences Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPreferences, MockUser, MockMovieRecomendation, MockSerieRecomendation, MockRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPreferences = jasmine.createSpy('MockPreferences');
            MockUser = jasmine.createSpy('MockUser');
            MockMovieRecomendation = jasmine.createSpy('MockMovieRecomendation');
            MockSerieRecomendation = jasmine.createSpy('MockSerieRecomendation');
            MockRequest = jasmine.createSpy('MockRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Preferences': MockPreferences,
                'User': MockUser,
                'MovieRecomendation': MockMovieRecomendation,
                'SerieRecomendation': MockSerieRecomendation,
                'Request': MockRequest
            };
            createController = function() {
                $injector.get('$controller')("PreferencesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'serflixApp:preferencesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

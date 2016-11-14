'use strict';

describe('Controller Tests', function() {

    describe('MusixiserFollow Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMusixiserFollow;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMusixiserFollow = jasmine.createSpy('MockMusixiserFollow');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MusixiserFollow': MockMusixiserFollow
            };
            createController = function() {
                $injector.get('$controller')("MusixiserFollowDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'musixiseApp:musixiserFollowUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

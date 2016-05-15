'use strict';

describe('Controller Tests', function() {

    describe('StagesFollow Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStagesFollow;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStagesFollow = jasmine.createSpy('MockStagesFollow');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StagesFollow': MockStagesFollow
            };
            createController = function() {
                $injector.get('$controller')("StagesFollowDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'musixiseApp:stagesFollowUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

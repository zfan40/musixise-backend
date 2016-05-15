(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('StagesDetailController', StagesDetailController);

    StagesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Stages'];

    function StagesDetailController($scope, $rootScope, $stateParams, entity, Stages) {
        var vm = this;
        vm.stages = entity;
        vm.load = function (id) {
            Stages.get({id: id}, function(result) {
                vm.stages = result;
            });
        };
        var unsubscribe = $rootScope.$on('musixiseApp:stagesUpdate', function(event, result) {
            vm.stages = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

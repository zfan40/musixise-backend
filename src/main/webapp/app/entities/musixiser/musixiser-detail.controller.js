(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('MusixiserDetailController', MusixiserDetailController);

    MusixiserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Musixiser', 'User'];

    function MusixiserDetailController($scope, $rootScope, $stateParams, entity, Musixiser, User) {
        var vm = this;
        vm.musixiser = entity;
        vm.load = function (id) {
            Musixiser.get({id: id}, function(result) {
                vm.musixiser = result;
            });
        };
        var unsubscribe = $rootScope.$on('musixiseApp:musixiserUpdate', function(event, result) {
            vm.musixiser = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

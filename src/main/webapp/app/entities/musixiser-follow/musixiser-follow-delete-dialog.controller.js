(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('MusixiserFollowDeleteController',MusixiserFollowDeleteController);

    MusixiserFollowDeleteController.$inject = ['$uibModalInstance', 'entity', 'MusixiserFollow'];

    function MusixiserFollowDeleteController($uibModalInstance, entity, MusixiserFollow) {
        var vm = this;
        vm.musixiserFollow = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            MusixiserFollow.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
